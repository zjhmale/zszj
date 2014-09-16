(ns zszj.db.articles
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db]
        clojure.walk)
  (:require [zszj.db.schema :as schema]))

(def all
  (select* articles))

(defn tagged [tag-name]
  (-> all (where {:tags tag-name})))

(defn find-articles-by-tag
  [tag]
  (select articles
          (where {:tags tag})))

;;just can see shit
;;(macroexpand-all '(tagged "cleantha"))

