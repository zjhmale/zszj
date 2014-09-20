(ns zszj.db.books
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(defn find-book-by-type
  [type limit-count]
  (select books
          (where {:book_type type})
          (limit limit-count)
          (order :updated_at :desc)))
