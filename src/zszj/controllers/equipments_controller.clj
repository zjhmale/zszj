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
        months (nth (split-at current-month months) 1)]
    (println "months: " months)
    (layout/render "equipments/index.html"
                   (common/common-manipulate {:years years
                                              :months months} "jgxx"))))

(split-at 3 '(1 2 3 4 5))

(defn search
  [& ajaxargs]
  "cleantha")
