(ns zszj.db.article-types
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(def all
  (select* article_types))

(defn with-key
  ([key]
     (-> all (where {:key key})))
  ([key1 key2]
     (-> all (where {:key key2
                     :parent_id (-> key1 with-key select first :id)}))))
