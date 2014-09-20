(ns zszj.controllers.zhongjies_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.db.books :as books]
            [zszj.views.helper :as helper]))

(def title-map
  {"zhongjies" "造价咨询企业"
   "zhaobiaos" "招标代理机构"
   "zjshis" "造价工程师"
   "gaisuans" "造价员"})

(defn index
  [type & params]
  (let [subtitle (get title-map type)]
    (println "type: " type "\nsubtitle: " subtitle)
    (layout/render "zhongjies/index.html"
                   (common/common-manipulate
                    {:type type
                     :subtitle subtitle} "zzzg"))))

(defn search
  [& ajaxargs]
  "cleantha")
