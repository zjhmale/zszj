(ns zszj.controllers.articles_controller
  (:require [zszj.layout :as layout]))

(defn articles-render
  [id]
  (println "article id is: " id)
  (layout/render "articles/show.html" {}))

