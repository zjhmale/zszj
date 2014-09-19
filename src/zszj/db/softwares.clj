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
          (limit limit-count)
          (offset offset-count)))

(defn get-software-by-id
  [id]
  (select softwares
          (where {:id id})))

