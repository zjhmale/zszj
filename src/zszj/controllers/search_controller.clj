(ns zszj.controllers.search-controller
  (:require [zszj.views.layout :as layout]))

(defn index
  []
  (layout/render "search/index.html"))

(defn search
  []
  (layout/render "search/index.html"))