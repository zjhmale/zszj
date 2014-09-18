(ns zszj.controllers.tezbs_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.views.helper :as helper]
            [zszj.db.tezbs :as tezbs]))

(def ^:dynamic *PER-PAGE* 20)

(def PER-PAGE 20)

(defn index
  [current-page]
  (let [zbs (map (fn [zb]
                   (assoc
                       (assoc zb :truncate_title (helper/truncate (:title zb) 20))
                     :show_addtime (helper/date-format (:addtime zb))))
                 (tezbs/get-tezbs (* (dec current-page) PER-PAGE) PER-PAGE))
        num-zbs (db/count-tezbs)]
    (layout/render "tezbs/index.html"
                   (common/common-manipulate
                    (merge {:zbs zbs
                            ;;for paginator
                            :current-page current-page
                            :current-page-dec (dec current-page)
                            :current-page-inc (inc current-page)
                            :num-articles num-zbs}
                           (common/paginator num-zbs PER-PAGE current-page "/gczjzbs")) "jgxx"))))

(defn show
  [id]
  (let [zb (tezbs/get-tezb-by-id id)]
    ;;(println zb)
    (layout/render "tezbs/show.html"
                   (common/common-manipulate
                    {:zb zb} "jgxx"))))
