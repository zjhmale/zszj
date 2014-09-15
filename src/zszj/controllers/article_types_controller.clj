(ns zszj.controllers.article_types_controller
  (:use [ring.util.response :only [redirect]])
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.db.core :as db]
            [selmer.parser :as parser]))

(def need-show-time? #{"news" "policy" "price_area" "public_info" "exam"})

(def ^:dynamic *PER-PAGE* 20)

(def PER-PAGE 20)

(def NUM-HEAD-LINKS 3)
(def NUM-TAIL-LINKS 3)
(def NUM-PREV-LINKS 4)
(def NUM-POST-LINKS 4)

(defn truncate [s length]
  (if (< (count s) length)
    s
    (->> s (take length) (apply str))))

(defn format-date [format date]
  (.format (java.text.SimpleDateFormat. format) date))

(defn first-container-article-type [type]
  (if (or (db/root? type) (= (:the_type type) "folder"))
    (first-container-article-type
     (first (db/child-article-types (:id type))))
    type))

(defn- identify-root-and-type [id]
  (let [type (db/article-type id)        
        root-type (db/root-article-type id)
        type (first-container-article-type type)]
    [root-type type])) 

(defn generate-new-articles
  [articles root-type]
  (map (fn [article]
         (assoc article :uri (helper/article-path article)))
       (map (fn [article]
              (assoc article :title (truncate (:title article) 100)))
            (map (fn [article]
                   (if (need-show-time? (:key root-type))
                     (assoc article :addtime (str "[" (format-date "yyyy-MM-dd" (:addtime article)) "] "))
                     (assoc article :addtime "")))
                 articles))))

(defn paginator
  [num-entry perpage curr-page base-uri]
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

(defn render
  [id current-page]
  (let [[root-type type] (identify-root-and-type id)
        articles (if (:tag type)
                   (db/paginage-articles-of-tag current-page *PER-PAGE* (:tag type))
                   (db/paginage-articles-of-type current-page *PER-PAGE* (:id type)))
        num-articles (if (:tag type)
                       (db/count-articles-of-tag (:tag type))
                       (db/count-articles-of-type(:id type)))
        current-root-key (:key root-type)
        root-id (:id root-type)
        level2s (db/child-article-types root-id)
        level2s-with-level3s-and-articles
        (map (fn [level2]
               (let [level2id (:id level2)
                     level3s (db/child-article-types level2id)
                     articles (db/articles-of-type level2id)]
                 (assoc (assoc level2 :level3s level3s) :articles articles))) level2s)]
    ;;(println "articles: " articles "\nroot-type: " root-type "\ntype: " type "\ncurrent-page: " current-page "\nnum-articles: " num-articles "\ncurrent-root-key: " current-root-key "\nlevel2s-with-level3s-and-articles: " level2s-with-level3s-and-articles "\nmenus: " layout/menus)
    (if (nil? type) nil
        (if (= (:the_type type) "direct_display")
          (redirect (str "/articles/" (:id (first articles))))
          (layout/render "article_types/show.html"
                         (merge {:articles (generate-new-articles articles root-type)
                                 :root-type root-type
                                 :type type
                                 :current-page current-page
                                 :current-page-dec (dec current-page)
                                 :current-page-inc (inc current-page)
                                 :num-articles num-articles
                                 ;;for navigator
                                 :level2s level2s-with-level3s-and-articles
                                 ;;for navibar
                                 :menus layout/menus
                                 :current-root-key current-root-key}
                                (paginator num-articles PER-PAGE current-page (str "/article_types/" (:id type)))))))))
