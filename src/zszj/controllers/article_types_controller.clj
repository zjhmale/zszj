(ns zszj.controllers.article_types_controller
  (:require [zszj.layout :as layout]
            [zszj.db.core :as db]
            [selmer.parser :as parser]))

(defn render
  [id current-page]
  (str "id: " id "\ncurrent-page: " current-page))

