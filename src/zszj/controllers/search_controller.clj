(ns zszj.controllers.search-controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.full-texts :as fulltexts]))

(defn index
  [request]
  (let [search_str (get-in request [:params :search :search_str])
        search_cat (get-in request [:params :search :search_cat])
        full_texts (fulltexts/search-by-cat search_str search_cat)]
    (layout/render "search/index.html"
                   (common/common-manipulate
                     {:cats       (fulltexts/get-cats)
                      :full_texts full_texts
                      :search_str search_str
                      :pretext    "pretext"
                      :highlight  "高亮"
                      :followtext "followtext"} ""))))
