(ns zszj.db.man_markets
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(defn get-year-season
  []
  (set (select man_markets
               (fields :year :season))))
