(ns zszj.controllers.softwares_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.views.helper :as helper]
            [zszj.db.core :as db]
            [zszj.db.softwares :as softwares]
            [zszj.views.helper :as helper]))

(def ^:dynamic *PER-PAGE* 20)

(def PER-PAGE 20)

(defn index
  [& params]
  ;;(println params)
  (let [type (-> (nth params 0) :software_type)
        current-page (-> (nth params 0) :page)
        current-page (bigdec (if current-page current-page "1"))
        softwares (map (fn [software]
                         (assoc
                             (assoc software :first-attachmentid (softwares/first-attachmentid (:id software)))
                           :truncate_title (helper/truncate (:title software) 80)))
                       (softwares/get-softwares-by-type
                        (* (dec current-page) PER-PAGE) PER-PAGE type))
        num-softwares (db/count-softwares type)]
    (println "type: " type "\ncurrent-page: " current-page "\nsoftwares: " softwares)
    (layout/render "softwares/index.html"
                   (common/common-manipulate
                    (merge {:first-software (first softwares)
                            :softwares softwares
                            ;;for paginator
                            :current-page current-page
                            :current-page-dec (dec current-page)
                            :current-page-inc (inc current-page)
                            :num-articles num-softwares}
                           (common/paginator num-softwares PER-PAGE current-page (str "/softwares?software_type=" type) "cleantha")) "zlxz"))))

(defn show
  [id]
  ;;(println id)
  (layout/render "softwares/show.html"
                 (common/common-manipulate
                  {} "zlxz")))

