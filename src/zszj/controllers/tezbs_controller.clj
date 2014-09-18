(ns zszj.controllers.tezbs_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.db.tezbs :as tezbs]))

(def ^:dynamic *PER-PAGE* 20)

(def PER-PAGE 20)

(defn index
  [current-page]
  (let [zbs (tezbs/get-tezbs (* (dec current-page) PER-PAGE) PER-PAGE)
        num-zbs (db/count-tezbs)]
    (layout/render "tezbs/index.html"
                   (common/common-manipulate
                    (merge {;;for paginator
                            :current-page current-page
                            :current-page-dec (dec current-page)
                            :current-page-inc (inc current-page)
                            :num-articles num-zbs}
                           (common/paginator num-zbs PER-PAGE current-page "/gczjzbs")) "jgxx"))))

(defn show
  [id]
  (layout/render "tezbs/show.html"
                 (common/common-manipulate {} "jgxx")))