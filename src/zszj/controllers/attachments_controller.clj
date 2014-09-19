(ns zszj.controllers.attachments_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.views.helper :as helper]
            [ring.util.response :as resp])
  (:import (java.net URLEncoder)))

(defn index
  []
  (layout/render "attachments/index.html" {}))

(defn send-file
  [id]
  (let [att (db/attachment id)
        raw-file-name (:filename att)
        file-name (URLEncoder/encode (:filename att) "UTF-8")
        content-type (:content_type att)
        raw-response (resp/resource-response (str "public/files/" raw-file-name))
        response (assoc raw-response :headers (merge (:headers raw-response)
                                                     {"Content-Type" content-type
                                                      "Content-Disposition" (str "attachment; filename=\"" file-name "\"; filename*=utf-8''" file-name)}))]
    (println file-name)
    (println response)
    response))

;;(URLEncoder/encode "三三" "UTF-8")

