(ns zszj.controllers.man_markets_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.man_markets :as man_markets]))

(def season-map
  {:1 "一"
   :2 "二"
   :3 "三"
   :4 "四"})

(defn index
  [& ajaxargs]
  (println ajaxargs)
  (let [all-year-seasons (sort-by :year >
                          (sort-by :season >
                                         (map (fn [pair]
                                                (assoc (assoc (assoc pair :chinese-season (get season-map (keyword (:season pair)))) :year (Integer/parseInt (:year pair))) :season (Integer/parseInt (:season pair))))
                                              (man_markets/get-year-season))))]
    ;;(println "all-year-seasons: " all-year-seasons)
    (layout/render "man_markets/index.html"
                   (common/common-manipulate {:all-year-seasons all-year-seasons} "jgxx"))))

(defn view
  []
  (println (slurp "resources/views/man_markets/views.html"))
  "cleantha")
