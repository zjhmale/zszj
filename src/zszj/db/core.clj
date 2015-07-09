(ns zszj.db.core
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config)
  (:require [zszj.db.schema :as schema]))

;;register database connection schema
(defdb db schema/db-spec)

(declare aricles article_types softwares links link_types)

;;one-to-many
(defentity articles
           (belongs-to article_types {:fk :article_type_id}))

(defentity article_types
           (has-many articles {:fk :article_type_id}))

(defentity softwares)

(defentity links
           (belongs-to link_types {:fk :link_type_id}))

(defentity link_types
           (has-many links {:fk :link_type_id}))

(defentity attachments)

(defentity materials)

(defentity man_markets)

(defentity man_costs)

(defentity equipment)

(defentity gczjzbs)

(defentity attachments)

(defentity books)

(defentity zhongjies)

(defentity zhaobiaos)

(defentity zjshis)

(defentity gaisuans)

(defentity full_texts)

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
            (where {:key key})
            (order :id :desc))))

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

;;for paginator
(defmacro paginate
  ([page num-per-page query]
   `(~@query
      (limit ~num-per-page)
      (offset (* (dec ~page) ~num-per-page)))))

(defn paginage-articles-of-type
  [page perpage type-id]
  (paginate page
            perpage
            (select articles
                    (where {:article_type_id type-id})
                    (order :id :desc))))

(defn paginage-articles-of-tag
  [page perpage tag]
  (paginate page
            perpage
            (select articles
                    (where {:tags tag})
                    (order :id :desc))))

(defmacro sql-count [query]
  `(:cnt
     (first
       (~@query
         (kc/aggregate (~'count :*) :cnt)))))

(defn count-articles-of-type
  [type-id]
  (sql-count
    (select articles
            (where {:article_type_id type-id}))))

(defn count-articles-of-tag
  [tag]
  (sql-count
    (select articles
            (where {:tags tag}))))

(defn count-tezbs
  []
  (sql-count
    (select gczjzbs)))

(defn count-softwares
  [type]
  (sql-count
    (select softwares
            (where {:software_type type}))))

(defn attachment [id]
  (first (select attachments
                 (where {:id id}))))
