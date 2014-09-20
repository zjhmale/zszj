(ns zszj.db.softwares
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(def all
  (select* softwares))

(defn home-softwares
  []
  (select softwares
          (order :addtime :desc)
          (limit 6)))

(defn get-softwares-by-type
  [offset-count limit-count type]
  (select softwares
          (where {:software_type type})
          (order :addtime :desc)
          (limit limit-count)
          (offset offset-count)))

(defn has-attachments
  [id]
  (empty? (select attachments
                  (where {:container_id id
                          :container_type "Software"}))))

(defn first-attachmentid
  [id]
  (:id (first
        (select attachments
                (where {:container_id id
                        :container_type "Software"})
                (order :updated_at :desc)))))

(defn get-software-by-id
  [id]
  (select softwares
          (where {:id id})))

