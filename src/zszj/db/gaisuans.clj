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
