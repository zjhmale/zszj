(ns zszj.controllers.books_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.views.helper :as helper]))

(defn index
  [& params]
  ;;(println params)
  (let [book_type (-> (nth params 0) :book_type)]
    (println book_type)
    "cleantha"))

(defn search
  [& ajaxargs]
  (println ajaxargs)
  "cleantha")
