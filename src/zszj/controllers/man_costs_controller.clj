(ns zszj.controllers.man_costs_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.man_costs :as man_costs]
            [selmer.parser :as parser]))

(defn index
  []
  (let [all-year-seasons (common/sort-year-and-season (man_costs/get-year-season))]
    (println "all-year-seasons: " all-year-seasons)
    (layout/render "man_costs/index.html"
                   (common/common-manipulate {:all-year-seasons all-year-seasons} "jgxx"))))

(defn search
  [& ajaxargs]
  "cleantha")
