(ns zszj.db.core
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config)
  (:require [zszj.db.schema :as schema]))

;;register database connection schema
(defdb db schema/db-spec)

(declare aricles article_types softwares)

;;one-to-many
(defentity articles
  (belongs-to article_types {:fk :article_type_id}))

(defentity article_types
  (has-many articles {:fk :article_type_id}))

(defn article [id]
  (first (select articles
                 (where {:id id}))))

(defn child-article-types [id]
  (select article_types
          (where {:parent_id id})))

(defn article-type [id]
  (first (select article_types
                 (where {:id id}))))

(defn article-type-by-key [key]
  (first 
   (select article_types
           (where {:key key}))))

(defn articles-of-type [type-id]
  (select articles
          (where {:article_type_id type-id})))

;;the parent_id of root type is nil
(defn root? [t] 
  (not (:parent_id t)))

(defn root-article-type [id]
  (let [t (article-type id)]
    (if (root? t)
      t
      (root-article-type (:parent_id t)))))

(defn parent-article-type [id]
  (article-type
   (:parent_id (article-type id))))
