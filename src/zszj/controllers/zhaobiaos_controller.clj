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

(defn search
  [& ajaxargs]
  (let [param (nth ajaxargs 0)
        search_str (-> param :search_str)
        search_field (-> param :search :field)
        current-page (-> param :page)
        current-page (bigdec (if current-page current-page "1"))
        num-zhaobiaos (zhaobiaos/get-zhaobiaos-count)
        base-uri (str "zhaobiaos/search?_=1411304244920&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&search[field]=" search_field "&search_str=" search_str)
        paginator-view (common/generate-paginator-html num-zhaobiaos base-uri current-page)]
    (str "jQuery(\"#view\").html(\"" paginator-view "\");\n"
         "jQuery(\"#view\").visualEffect(\"slide_down\");")))

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
