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

(defn search-book
  [search limit-count]
  (vec (set (apply merge
                   (apply merge
                          (apply merge
                                 (select books
                                         (where {:book_type [like (str "%" search "%")]})
                                         (limit limit-count)
                                         (order :updated_at :desc))
                                 (select books
                                         (where {:name [like (str "%" search "%")]})
                                         (limit limit-count)
                                         (order :updated_at :desc)))
                          (select books
                                  (where {:price [like (str "%" search "%")]})
                                  (limit limit-count)
                                  (order :updated_at :desc)))
                   (select books
                           (where {:press [like (str "%" search "%")]})
                           (limit limit-count)
                           (order :updated_at :desc))))))

;;(merge {:a 1} {:b 2} {:c 3})
(merge {:a 1} {:a 1})
(vec (set (apply merge '({:a 1}) '({:a 1}))))
(concat '({:a 1}) '({:a 1}))
;;(apply merge (apply merge '({:a 1}) '({:b 2})) '({:c 3}))


