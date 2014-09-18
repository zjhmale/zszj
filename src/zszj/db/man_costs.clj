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
