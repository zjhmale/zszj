(ns zszj.db.full-texts
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(def all
  (select* article_types))

(defn get-cats
  []
  (distinct (map :cat (select full_texts
                              (where {:cat [not= nil]})
                              (order :cat)))))

(defn search-by-cat
  [search_str search_cat offset-count limit-count]
  (if (and (empty? search_str)
           (empty? search_cat))
    (select full_texts
            (limit limit-count)
            (offset offset-count))
    (if (and (empty? search_str)
             (not (empty? search_cat)))
      (select full_texts
              (where {:cat search_cat})
              (limit limit-count)
              (offset offset-count))
      (if (and (not (empty? search_str))
               (empty? search_cat))
        (select full_texts
                (where {:full_text [like (str "%" search_str "%")]})
                (limit limit-count)
                (offset offset-count))
        (select full_texts
                (where {:full_text [like (str "%" search_str "%")]
                        :cat       search_cat})
                (limit limit-count)
                (offset offset-count))))))

(defn get-fulltexts-count
  []
  (sql-count
    (select full_texts)))