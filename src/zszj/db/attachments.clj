(ns zszj.db.attachments
  (:use [korma.core :as kc]
        [korma.db :only (defdb)]
        korma.config
        [zszj.db.core :as db])
  (:require [zszj.db.schema :as schema]))

(defn find-first-attachment-by-articleid
  [articleid]
  (first
   (select attachments
           (where {:container_type "Article"
                   :container_id articleid}))))
