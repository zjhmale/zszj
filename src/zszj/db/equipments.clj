(ns zszj.db.equipments
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(defn get-years
  []
  (set (select equipment
               (fields :year))))

(defn get-months
  []
  (set (select equipment
               (fields :month))))
