(ns zszj.controllers.materials_controller
  (:require [cheshire.core :as ches]
            [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.db.materials :as materials]
            [zszj.controllers.common :as common]))

(defn index
  []
  (let [material-names (materials/get-material-field :name)
        material-specs (materials/get-material-field :spec)
        latest-material-date (helper/date-format-without-brackets
                              (:publish_at (materials/get-latest-material)))]
    ;;(println "names: " material-names "\nspecs: " material-specs)
    (layout/render "materials/index.html"
                   (common/common-manipulate {:material-names material-names
                                              :material-specs material-specs
                                              :latest-material-date latest-material-date} "jgxx"))))

(defn- mini_view
  [publish_at]
  (let [materials (map (fn [material]
                         (assoc
                             (assoc material :truncate_name (helper/truncate (:name material) 6))
                           :truncate_spec_brand (helper/truncate (str (:spec material) "-" (:brand material)) 10)))
                       (materials/get-materials-by-publish publish_at 11))
        material-table (reduce (fn [html material]
                                 (str html "<tr><td title=\\\"" (:name material) "\\\">" (:truncate_name material) "</td><td title=\\\"" (str (:spec material) "-" (:brand material)) "\\\">" (:truncate_spec_brand material) "</td><td>" (:price material) "</td></tr>")) "" materials)]
    ;;(println material-table)
    (str "\"<table width=\\\"200\\\" class=\\\"cx\\\" ><tr class=\\\"t2\\\"><th>名 称</th><th>规 格</th><th>价 格</th></tr>" material-table "<tr><td class=\\\"more\\\"></td><td class=\\\"more\\\"></td><td class=\\\"more\\\"><a href=\\\"/materials\\\" class=\\\"black t2\\\">...更多</a></td></tr></table>\"")))

(defn mini_search
  [& ajaxargs]
  (let [publish_at (-> (nth ajaxargs 0) :selected :publish_at)]
    ;;(println publish_at)
    (str "jQuery(\"#mini_material_view\").html(" (mini_view publish_at) ");\n"
         "jQuery(\"#mini_material_view\").visualEffect(\"slide_down\");")))

(defn- view
  [publish_at name spec]
  )

(defn search
  [& ajaxargs]
  (let [param (nth ajaxargs 0)
        publish_at (-> param :selected :publish_at)
        name (-> param :selected :name)
        spec (-> param :selected :spec)
        materials (materials/get-materials-for-view publish_at name spec)]
    (println "publish_at: " publish_at "\nname: " name "\nspec: " spec)
    (str "jQuery(\"#view\").html(" "\"cleantha\"" ");\n"
         "jQuery(\"#view\").visualEffect(\"slide_down\");")))

;;just show how to use with jquery datepicker
(defn datepicker
  []
  (layout/render "materials/datepicker.html" {}))





