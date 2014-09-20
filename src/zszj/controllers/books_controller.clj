(ns zszj.controllers.books_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.db.books :as books]
            [zszj.views.helper :as helper]))

(def ^:dynamic *PER-PAGE* 20)

(def PER-PAGE 20)

(defn index
  [& params]
  ;;(println params)
  (let [book_type (-> (nth params 0) :book_type)
        books (common/assoc-index-oddeven (books/find-book-by-type book_type PER-PAGE))]
    (println "booktype: " book_type "\nbooks: " books "\nfirstbook: " (first books))
    (layout/render "books/index.html"
                   (common/common-manipulate
                    (merge {:books books
                            :first-book (first books)}
                           (common/paginator 20 PER-PAGE 1 (str "/books"))) "wssx"))))

(defn search
  [& ajaxargs]
  (println ajaxargs)
  "cleantha")
