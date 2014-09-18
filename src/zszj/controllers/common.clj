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

(defn- add-navibar
  [var-map current-root-key]
  (merge var-map
         {:menus layout/menus
          :current-root-key current-root-key}))

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
       {:base-uri base-uri
        :from (inc (* (dec curr-page) perpage))
        :to (min (* curr-page perpage) num-entry)
        :links-from links-from
        :links-to links-to
        :is-last-page (>= curr-page num-pages)
        :head-links (range 1 (inc (min (- links-from 1)
                                       NUM-HEAD-LINKS)))
        :tail-links (range (max (- num-pages NUM-TAIL-LINKS)
                                (+ links-to 1)) num-pages)
        :from-current-links (range links-from curr-page)
        :current-to-links (range (inc curr-page) (inc links-to))}))
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
       {:base-uri base-uri
        :from (inc (* (dec curr-page) perpage))
        :to (min (* curr-page perpage) num-entry)
        :links-from links-from
        :links-to links-to
        :is-last-page (>= curr-page num-pages)
        :head-links (range 1 (inc (min (- links-from 1)
                                       NUM-HEAD-LINKS)))
        :tail-links (range (max (- num-pages NUM-TAIL-LINKS)
                                (+ links-to 1)) num-pages)
        :from-current-links (range links-from curr-page)
        :current-to-links (range (inc curr-page) (inc links-to))
        :not-all-empty true})))

;;for the first time do the ajax job and then use the paginator for not-all-empty state
(def is-paginate-for-notallempty (atom false))
