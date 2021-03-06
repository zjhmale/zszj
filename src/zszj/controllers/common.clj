(ns zszj.controllers.common
  (:require [zszj.views.layout :as layout])
  (:import (java.util Calendar GregorianCalendar)
           (java.util Date)
           (java.sql Timestamp)))

(def weekday-map
  {:1 "日"
   :2 "一"
   :3 "二"
   :4 "三"
   :5 "四"
   :6 "五"
   :7 "六"})

(def season-map
  {:1 "一"
   :2 "二"
   :3 "三"
   :4 "四"})

(defn sort-year-and-season
  [year-seasons]
  (sort-by :year >
           (sort-by :season >
                    (map (fn [pair]
                           (assoc (assoc (assoc pair :chinese-season (get season-map (keyword (:season pair)))) :year (Integer/parseInt (:year pair))) :season (Integer/parseInt (:season pair))))
                         year-seasons))))

(defn assoc-index-oddeven
  [collections]
  (map (fn [item]
         (let [item-index (inc (.indexOf (vec collections) item))
               odd-even (if (odd? item-index)
                          "odd"
                          "even")]
           (assoc (assoc item :odd-even odd-even) :item-index item-index)))
       collections))

(defn assoc-index-oddeven-with-paginator
  [collections current-page perpage]
  (map (fn [item]
         (let [item-index (+ (* (dec current-page) perpage) (inc (.indexOf (vec collections) item)))
               odd-even (if (odd? item-index)
                          "odd"
                          "even")]
           (assoc (assoc item :odd-even odd-even) :item-index item-index)))
       collections))

(defn- get-current-weekday
  []
  (let [gc (GregorianCalendar.)
        day-of-week (.get gc Calendar/DAY_OF_WEEK)
        is-weekend (or (= day-of-week Calendar/SATURDAY) (= day-of-week Calendar/SUNDAY))]
    day-of-week))

(defn- get-current-datetime
  []
  (let [time (clojure.string/split
               (nth (clojure.string/split
                      (str (Timestamp. (.getTime (Date.)))) #" ") 0) #"-")
        year (nth time 0)
        month (nth time 1)
        day (nth time 2)]
    (str year "年" month "月" day "日")))

(defn get-current-year
  []
  (let [time (clojure.string/split
               (nth (clojure.string/split
                      (str (Timestamp. (.getTime (Date.)))) #" ") 0) #"-")
        year (nth time 0)]
    (Integer/parseInt year)))

(defn get-current-month
  []
  (let [time (clojure.string/split
               (nth (clojure.string/split
                      (str (Timestamp. (.getTime (Date.)))) #" ") 0) #"-")
        month (nth time 1)]
    (Integer/parseInt month)))

(defn- add-navibar
  [var-map current-root-key]
  (merge var-map
         {:menus            layout/menus
          :current-root-key current-root-key}))

(def PER-PAGE 20)
(def NUM-HEAD-LINKS 3)
(def NUM-TAIL-LINKS 3)
(def NUM-PREV-LINKS 4)
(def NUM-POST-LINKS 4)

(defn- add-datetime
  [var-map]
  (merge var-map
         {:week-day (get weekday-map (keyword (str (get-current-weekday))))
          :datetime (get-current-datetime)}))

(defn common-manipulate
  [var-map current-root-key]
  (-> var-map
      (add-navibar current-root-key)
      add-datetime))

(defn paginator
  ([num-entry perpage curr-page base-uri]
   (let [num-pages (-> (/ num-entry perpage) int inc)
         links-from (- curr-page NUM-PREV-LINKS)
         links-from (if (< links-from 1)
                      1
                      links-from)
         links-to (+ curr-page NUM-POST-LINKS)
         links-to (if (> links-to num-pages) num-pages
                                             links-to)
         head-links (range 1 (inc (min (- links-from 1)
                                       NUM-HEAD-LINKS)))
         tail-links (range (max (- num-pages NUM-TAIL-LINKS)
                                (+ links-to 1)) num-pages)
         from-current-links (range links-from curr-page)
         current-to-links (range (inc curr-page) (inc links-to))]
     ;;(println "head-links: " head-links "\ntail-links: " tail-links "\nfrom-current-links: " from-current-links "\ncurrent-to-links: " current-to-links "\nis-last-page?: " (>= curr-page num-pages))
     {:base-uri           base-uri
      :from               (inc (* (dec curr-page) perpage))
      :to                 (min (* curr-page perpage) num-entry)
      :links-from         links-from
      :links-to           links-to
      :is-last-page       (>= curr-page num-pages)
      :head-links         (range 1 (inc (min (- links-from 1)
                                             NUM-HEAD-LINKS)))
      :tail-links         (range (max (- num-pages NUM-TAIL-LINKS)
                                      (+ links-to 1)) num-pages)
      :from-current-links (range links-from curr-page)
      :current-to-links   (range (inc curr-page) (inc links-to))}))
  ([num-entry perpage curr-page base-uri notallempty]
   (let [num-pages (-> (/ num-entry perpage) int inc)
         links-from (- curr-page NUM-PREV-LINKS)
         links-from (if (< links-from 1)
                      1
                      links-from)
         links-to (+ curr-page NUM-POST-LINKS)
         links-to (if (> links-to num-pages) num-pages
                                             links-to)
         head-links (range 1 (inc (min (- links-from 1)
                                       NUM-HEAD-LINKS)))
         tail-links (range (max (- num-pages NUM-TAIL-LINKS)
                                (+ links-to 1)) num-pages)
         from-current-links (range links-from curr-page)
         current-to-links (range (inc curr-page) (inc links-to))]
     ;;(println "head-links: " head-links "\ntail-links: " tail-links "\nfrom-current-links: " from-current-links "\ncurrent-to-links: " current-to-links "\nis-last-page?: " (>= curr-page num-pages))
     {:base-uri           base-uri
      :from               (inc (* (dec curr-page) perpage))
      :to                 (min (* curr-page perpage) num-entry)
      :links-from         links-from
      :links-to           links-to
      :is-last-page       (>= curr-page num-pages)
      :head-links         (range 1 (inc (min (- links-from 1)
                                             NUM-HEAD-LINKS)))
      :tail-links         (range (max (- num-pages NUM-TAIL-LINKS)
                                      (+ links-to 1)) num-pages)
      :from-current-links (range links-from curr-page)
      :current-to-links   (range (inc curr-page) (inc links-to))
      :not-all-empty      true})))

(defn- generate-html
  [collections base-uri]
  (reduce (fn [html link]
            (str html "<a href=\\\"" base-uri "&page=" link "\\\">" link "</a><span> </span>")) "" collections))

(defn generate-paginator-html
  [num-item base-uri current-page]
  (let [num-pages (-> (/ num-item PER-PAGE) int inc)
        links-from (- current-page NUM-PREV-LINKS)
        links-from (if (< links-from 1)
                     1
                     links-from)
        links-to (+ current-page NUM-POST-LINKS)
        links-to (if (> links-to num-pages) num-pages
                                            links-to)
        head-links (range 1 (inc (min (- links-from 1)
                                      NUM-HEAD-LINKS)))
        tail-links (range (max (- num-pages NUM-TAIL-LINKS)
                               (+ links-to 1)) num-pages)
        from-current-links (range links-from current-page)
        current-to-links (range (inc current-page) (inc links-to))
        current-page-dec (dec current-page)
        current-page-inc (inc current-page)
        ;;base-uri (str "materials?_=1410958445083&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&selected[name]=" name "&selected[publish_at]=" publish_at "&selected[spec]=" spec "")
        from (inc (* (dec current-page) PER-PAGE))
        to (min (* current-page PER-PAGE) num-item)
        is-last-page (>= current-page num-pages)
        head-links-html (generate-html head-links base-uri)
        from-current-links-html (generate-html from-current-links base-uri)
        current-to-links-html (generate-html current-to-links base-uri)
        tail-links-html (generate-html tail-links base-uri)]
    (str "<div class=\\\"paginate\\\">显示<b>" from " - " to ", 共 " num-item " 条记录</b><span> </span><div class=\\\"pagination\\\">" (if (< current-page 2) (str "<span class=\\\"prev_page\\\">« 上一页</span>") (str "<a class=\\\"prev_page\\\" rel=\\\"prev start\\\" href=\\\"" base-uri "&page=" current-page-dec "\\\">« 上一页</a>")) "<span> </span>" head-links-html from-current-links-html "<span class=\\\"current\\\">" current-page "</span>" current-to-links-html tail-links-html (if is-last-page (str "<span class=\\\"disabled next_page\\\">下一页 »</span>") (str "<a class=\\\"next_page\\\" rel=\\\"next\\\" href=\\\"" base-uri "&page=" current-page-inc "\\\">下一页 »</a>")) "</div></div>")))

;;for the first time do the ajax job and then use the paginator for not-all-empty state
(def is-paginate-for-notallempty (atom false))
