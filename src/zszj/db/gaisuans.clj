(ns zszj.db.gaisuans
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(defn get-gaisuans
  [offset-count limit-count]
  (select gaisuans
          (order :zige :asc)
          (limit limit-count)
          (offset offset-count)))

(defn get-gaisuans-count
  []
  (sql-count
   (select gaisuans)))

(defn get-latest-updatetime
  []
  (:updated_at
   (first (select gaisuans
                  (fields :updated_at)
                  (order :updated_at :desc)))))

(defn get-zhaobiao-by-id
  [id]
  (first (select gaisuans
                 (where {:id id}))))

(defn find-gaisuans-by-field
  [field content offset-count limit-count]
  (select gaisuans
          (where {(keyword field) [like (str "%" content "%")]})
          (limit limit-count)
          (offset offset-count)))

(defn gaisuans-count-by-field
  [field content]
  (sql-count
   (select gaisuans
           (where {(keyword field) [like (str "%" content "%")]}))))
