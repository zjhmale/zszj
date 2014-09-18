(ns zszj.controllers.equipments_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.equipments :as equipments]
            [selmer.parser :as parser]))

(defn index
  []
  (let [years (sort-by :year > (map (fn [year]
                                      (assoc year :year (Integer/parseInt (:year year))))
                                    (equipments/get-years)))
        months (sort-by :month > (map (fn [month]
                                        (assoc month :month (Integer/parseInt (:month month))))
                                      (equipments/get-months)))
        current-month (common/get-current-month)
        months (nth (split-at current-month months) 1)
        latest-year-month (equipments/get-latest-year-month)
        year (:year latest-year-month)
        month (:month latest-year-month)
        items (common/assoc-index-oddeven (equipments/find-all-by-year-and-month year month))]
    (println "months: " months)
    (layout/render "equipments/index.html"
                   (common/common-manipulate {:year year
                                              :month month
                                              :years years
                                              :months months
                                              :items items} "jgxx"))))

(split-at 3 '(1 2 3 4 5))

(defn search
  [& ajaxargs]
  "cleantha")
