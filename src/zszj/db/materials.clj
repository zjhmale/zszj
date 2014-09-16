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
