(ns zszj.controllers.gaisuans_controller
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.db.zhaobiaos :as zhaobiaos]
            [selmer.parser :as parser]))

(defn search
  [& ajaxargs]
  (str "jQuery(\"#view\").visualEffect(\"slide_down\");"))