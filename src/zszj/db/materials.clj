(ns zszj.db.materials
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(defn get-latest-material
  []
  (first
   (select materials
           (order :publish_at :desc))))

(defn get-materials
  [limit-count]
  (select materials
          (order :publish_at :desc)
          (limit limit-count)))

;;find materials with publish_at datetime
(defn get-materials-by-publish
  [publish limit-count]
  (select materials
          (where {:publish_at publish})
          (order :publish_at :desc)
          (limit limit-count)))

(defn get-material-field
  [fieldname]
  (vec (set
        (map fieldname
             (select materials
                     (fields fieldname))))))

(defn get-materials-for-view
  [publish_at name spec]
  (let [conditions (merge (merge {:publish_at publish_at}
                                 (if (not (empty? name))
                                   {:name name}))
                          (if (not (empty? spec))
                            {:spec spec}))]
    (select materials
            (where conditions)
            (order :publish_at :desc)
            (limit 20))))

(defn get-materials-for-view-with-offset
  [current-page publish_at name spec]
  (let [conditions (merge (merge {:publish_at publish_at}
                                 (if (not (empty? name))
                                   {:name name}))
                          (if (not (empty? spec))
                            {:spec spec}))]
    (select materials
            (where conditions)
            (limit 20)
            )))

(defn get-materials-count-for-view
  [publish_at name spec]
  (let [conditions (merge (merge {:publish_at publish_at}
                                 (if (not (empty? name))
                                   {:name name}))
                          (if (not (empty? spec))
                            {:spec spec}))]
    (sql-count (select materials
                       (where conditions)))))

(defn get-materials-count
  []
  (sql-count
   (select materials
           (order :publish_at :desc))))
