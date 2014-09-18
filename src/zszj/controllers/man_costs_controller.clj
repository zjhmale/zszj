(ns zszj.controllers.man_costs_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.man_costs :as man_costs]
            [selmer.parser :as parser]))

(defn index
  []
  (let [all-year-seasons (common/sort-year-and-season (man_costs/get-year-season))
        latest-year-season (man_costs/get-latest-year-season)
        year (:year latest-year-season)
        season (:season latest-year-season)
        items (common/assoc-index-oddeven (man_costs/find-all-by-year-and-season year season))]
    (println "all-year-seasons: " all-year-seasons "\nyear: " year "\nseason: " season "\nitems: " items)
    (layout/render "man_costs/index.html"
                   (common/common-manipulate {:all-year-seasons all-year-seasons
                                              :year year
                                              :season season
                                              :items items} "jgxx"))))

(defn search
  [& ajaxargs]
  (str "jQuery(\"#view\").visualEffect(\"slide_down\");"))
