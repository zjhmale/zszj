(ns zszj.controllers.zhaobiaos_controller
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.db.zhaobiaos :as zhaobiaos]
            [selmer.parser :as parser]))

(defn- view-from-template
  [zhaobiao]
  (parser/render (slurp "resources/views/zhaobiaos/info.html")
                 {:zhaobiao zhaobiao}))

(defn show
  [id]
  (let [zhaobiao (zhaobiaos/get-zhaobiao-by-id id)]
    (layout/render "zhaobiaos/show.html"
                   (common/common-manipulate
                    {:zhaobiao zhaobiao} "zzzg"))))

(defn info
  [id ajaxargs]
  ;;(println id)
  ;;(println ajaxargs)
  (let [zhaobiao (zhaobiaos/get-zhaobiao-by-id id)]
    (view-from-template zhaobiao)))
