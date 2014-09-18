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

(defn get-latest-year-month
  []
  (first (select equipment
                 (fields :year :month)
                 (order :month :desc)
                 (order :year :desc))))

(defn find-all-by-year-and-month
  [year month]
  (set (select equipment
               (where {:year year
                       :month month})
               (order :year :desc)
               (order :month :desc))))

