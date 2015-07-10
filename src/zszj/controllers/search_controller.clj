(ns zszj.controllers.search-controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.full-texts :as fulltexts]))

(defn index
  []
  (layout/render "search/index.html"
                 (common/common-manipulate
                   {:type       "search"
                    :cats       (fulltexts/get-cats)
                    :pretext    "pretext"
                    :highlight  "高亮"
                    :followtext "followtext"} "")))

(defn search
  []
  (layout/render "search/index.html"
                 (common/common-manipulate
                   {:type "search"
                    :cats (fulltexts/get-cats)} "")))