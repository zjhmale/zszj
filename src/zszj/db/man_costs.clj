(ns zszj.db.man_costs
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(defn get-year-season
  []
  (set (select man_costs
               (fields :year :season))))

(defn get-latest-year-season
  []
  (first (select man_costs
                 (fields :year :season)
                 (order :season :desc)
                 (order :year :desc))))

(defn find-all-by-year-and-season
  [year season]
  (set (select man_costs
               (where {:year   year
                       :season season})
               (order :year :desc)
               (order :season :desc))))
