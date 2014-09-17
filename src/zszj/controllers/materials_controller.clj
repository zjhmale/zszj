(ns zszj.controllers.materials_controller
  (:require [cheshire.core :as ches]
            [zszj.views.layout :as layout]))

(defn index
  []
  "cleantha")

(defn- mini_view
  []
  "\"<table width=\\\"200\\\" class=\\\"cx\\\" ><tr class=\\\"t2\\\"><th>名 称</th><th>规 格</th><th>价 格</th></tr><tr><td class=\\\"more\\\"></td></td><td class=\\\"more\\\"></td></td><td class=\\\"more\\\"></table>\"")


(defn mini_search
  [& ajaxargs]
  (println ajaxargs)
  (str "jQuery(\"#mini_material_view\").html(" (mini_view) ");\n"
       "jQuery(\"#mini_material_view\").visualEffect(\"slide_down\");"))

(defn datepicker
  []
  (layout/render "materials/datepicker.html" {}))
