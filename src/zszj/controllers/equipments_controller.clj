(ns zszj.controllers.equipments_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [selmer.parser :as parser]))

(defn index
  []
  (layout/render "equipments/index.html" {} "jgxx"))

(defn search
  [& ajaxargs]
  "cleantha")
