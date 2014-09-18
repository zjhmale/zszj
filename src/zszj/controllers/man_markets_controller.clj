(ns zszj.controllers.man_markets_controller
  (:require [zszj.views.layout :as layout]
           [zszj.controllers.common :as common]))

(defn index
  []
  (layout/render "man_markets/index.html"
                 (common/common-manipulate {} "jgxx")))

(defn view
  []
  (println (slurp "resources/views/man_markets/views.html"))
  "cleantha")
