(ns zszj.db.zhaobiaos
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(defn get-zhaobiaos
  [offset-count limit-count]
  (select zhaobiaos
          (order :addtime :desc)
          (order :zizhi_no :asc)
          (order :register_no :asc)
          (limit limit-count)
          (offset offset-count)))

(defn get-zhaobiaos-count
  []
  (sql-count
   (select zhaobiaos)))

(defn get-latest-updatetime
  []
  (:updated_at
   (first (select zhaobiaos
                  (fields :updated_at)
                  (order :updated_at :desc)))))

(defn get-zhaobiao-by-id
  [id]
  (first (select zhaobiaos
                 (where {:id id}))))

(defn find-zhaobiaos-by-field
  [field content offset-count limit-count]
  (select zhaobiaos
          (where {(keyword field) [like (str "%" content "%")]})
          (limit limit-count)
          (offset offset-count)))

(defn zhaobiaos-count-by-field
  [field content]
  (sql-count
   (select zhaobiaos
           (where {(keyword field) [like (str "%" content "%")]}))))
