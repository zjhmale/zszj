(ns zszj.controllers.man_costs_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [selmer.parser :as parser]))

(defn index
  []
  (layout/render "man_costs/index.html"
                 (common/common-manipulate {} "jgxx")))

(defn search
  [& ajaxargs]
  "cleantha")
