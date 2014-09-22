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

;;deprecated it's not concise and elegant
;;(defn search-book
;;  [search limit-count]
;;  (vec (set (apply merge
;;                   (apply merge
;;                          (apply merge
;;                                 (select books
;;                                         (where {:book_type [like (str "%" search "%")]})
;;                                         (limit limit-count)
;;                                         (order :updated_at :desc))
;;                                 (select books
;;                                         (where {:name [like (str "%" search "%")]})
;;                                         (limit limit-count)
;;                                         (order :updated_at :desc)))
;;                          (select books
;;                                  (where {:price [like (str "%" search "%")]})
;;                                  (limit limit-count)
;;                                  (order :updated_at :desc)))
;;                   (select books
;;                           (where {:press [like (str "%" search "%")]})
;;                           (limit limit-count)
;;                           (order :updated_at :desc))))))

;;(merge {:a 1} {:b 2} {:c 3})
;;(merge {:a 1} {:a 1})
;;(vec (set (apply merge '({:a 1}) '({:a 1}))))
;;(concat '({:a 1}) '({:a 1}))
;;(apply merge (apply merge '({:a 1}) '({:b 2})) '({:c 3}))

;;cause the result by select function returned is a list of map so should use apply merge instead of simply use merge
;;extract public function
(defn search-book-public-fn
  [search limit-count]
  (letfn [(match [field]
            (select books
                    (where {field [like (str "%" search "%")]})
                    (limit limit-count)
                    (order :updated_at :desc)))]
    (vec (set (apply merge
                     (apply merge
                            (apply merge
                                   (match :book_type)
                                   (match :name))
                            (match :price))
                     (match :press))))))

;;clojure idiomatic
(defn search-book
  [search limit-count]
  (letfn [(match [field]
            (select books
                    (where {field [like (str "%" search "%")]})
                    (limit limit-count)
                    (order :updated_at :desc)))]
    (vec (set (reduce (fn [old-map new-map]
                        (apply merge old-map new-map))
                      (map match [:book_type :name :price :press]))))))
