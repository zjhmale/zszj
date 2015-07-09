(ns zszj.db.zjshis
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(defn get-zjshis
  [offset-count limit-count]
  (select zjshis
          (order :zhengshu :asc)
          (limit limit-count)
          (offset offset-count)))

(defn get-zjshis-count
  []
  (sql-count
    (select zjshis)))

(defn get-latest-updatetime
  []
  (:updated_at
    (first (select zjshis
                   (fields :updated_at)
                   (order :updated_at :desc)))))

(defn get-zhaobiao-by-id
  [id]
  (first (select zjshis
                 (where {:id id}))))

(defn find-zjshis-by-field
  [field content offset-count limit-count]
  (select zjshis
          (where {(keyword field) [like (str "%" content "%")]})
          (limit limit-count)
          (offset offset-count)))

(defn zjshis-count-by-field
  [field content]
  (sql-count
    (select zjshis
            (where {(keyword field) [like (str "%" content "%")]}))))
