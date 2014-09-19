(ns zszj.controllers.softwares_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
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
        softwares (softwares/get-softwares-by-type
                   (* (dec current-page) PER-PAGE) PER-PAGE type)]
    (println "type: " type "\ncurrent-page: " current-page "\nsoftwares: " softwares)
    (layout/render "softwares/index.html"
                   (common/common-manipulate
                    {} "zlxz"))))

(defn show
  [id]
  ;;(println id)
  (layout/render "softwares/show.html"
                 (common/common-manipulate
                  {} "zlxz")))

