(ns zszj.db.links
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(def all (select* links))

(defn find-linktypeid-by-subject
  [subject]
  (:id (first
        (select link_types
                (fields :id)
                (where {:subject subject})))))

(defn find-links-by-linktypeid
  [typeid]
  (select links
          (fields :link_name :link_url)
          (where {:link_type_id typeid})))

(defn with-type [type-subject]
  (let [t (first (select link_types
                  (where {:subject type-subject})
                  (limit 1)))]
    (-> links (where {:link_type_id (:id t)}))))
