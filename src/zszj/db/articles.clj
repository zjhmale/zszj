(ns zszj.db.articles
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db]
        clojure.walk)
  (:require [zszj.db.schema :as schema]
            [zszj.db.article-types :as article-types]))

(def all
  (select* articles))

(defn tagged [tag-name]
  (-> all (where {:tags tag-name})))

(defn find-articles-by-tag
  ([tag]
     (select articles
             (where {:tags tag})))
  ([tag limit-count]
     (select articles
             (where {:tags tag})
             (order :addtime :desc)
             (limit limit-count))))

(defn home-article-by-typeid-and-limit
  [typeid limit-count]
  (select articles
          (where {:article_type_id typeid})
          (limit limit-count)
          (order :addtime :desc)))

(defn find-articles-by-tags
  [level1 level2 limit-count]
  (let [article-type (article-types/find-type level1 level2)]
    (select articles
            (where {:article_type_id (:id (first article-type))})
            (limit limit-count))))

;;just can see shit
;;(macroexpand-all '(tagged "cleantha"))

