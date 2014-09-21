(ns zszj.db.zhongjies
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))


(defn get-zhongjies
  [offset-count limit-count]
  (select zhongjies
          (order :addtime :desc)
          (order :zizhi_no :asc)
          (order :register_no :asc)
          (limit limit-count)
          (offset offset-count)))

(defn get-zhongjies-count
  []
  (sql-count
   (select zhongjies)))

(defn get-latest-updatetime
  []
  (:updated_at
   (first (select zhongjies
                              (fields :updated_at)
                              (order :updated_at :desc)))))
