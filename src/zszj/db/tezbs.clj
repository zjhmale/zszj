(ns zszj.db.tezbs
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(defn get-tezbs
  [offset-count limit-count]
  (select gczjzbs
          (order :addtime :desc)
          (limit limit-count)))
