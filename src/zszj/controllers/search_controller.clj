(ns zszj.controllers.search-controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.full-texts :as fulltexts]))

(defn highlight-search-text
  [full_texts search_str]
  (map (fn [fulltext]
         (let [offset 60
               parts (clojure.string/split (:full_text fulltext) (re-pattern search_str))
               pretext-raw (nth parts 0)
               followtext-raw (nth parts 1)
               preindex (- (count pretext-raw) offset)
               pretext (if (> preindex 0)
                         (str "... " (subs pretext-raw preindex))
                         pretext-raw)
               followtext (if (< offset (count followtext-raw))
                            (str (subs followtext-raw 0 offset) " ...")
                            followtext-raw)]
           (-> fulltext
               (assoc :pretext pretext)
               (assoc :followtext followtext)))) full_texts))

(defn index
  [request]
  (let [search_str (get-in request [:params :search :search_str])
        search_cat (get-in request [:params :search :search_cat])
        current-page (let [page_raw (get-in request [:params :page])]
                       (if page_raw
                         (bigdec page_raw)
                         1))
        x (get-in request [:params :x])
        y (get-in request [:params :y])
        num-fulltexts (fulltexts/get-fulltexts-count)
        full_texts (highlight-search-text
                     (fulltexts/search-by-cat search_str search_cat (* (dec current-page) common/PER-PAGE) common/PER-PAGE)
                     search_str)]
    (do (prn (str "current-page -> " current-page))
        (layout/render "search/index.html"
                       (common/common-manipulate
                         (merge {:cats       (fulltexts/get-cats)
                                 :full_texts full_texts
                                 :search_str search_str
                                 :search_cat search_cat}
                                (let [base-uri (str "/search?search%5Bsearch_str%5D=" search_str "&search%5Bsearch_cat%5D=" search_cat "&x=" x "&y=" y)]
                                  (common/paginator num-fulltexts common/PER-PAGE current-page base-uri "notallempty"))) "")))))