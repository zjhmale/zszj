(ns zszj.controllers.softwares_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.views.helper :as helper]))
 
(defn index
  [& params]
  (layout/render "softwares/index.html"
                 (common/common-manipulate
                  {} "zlxz")))

(defn show
  [id]
  (layout/render "softwares/show.html"
                 (common/common-manipulate
                  {} "zlxz")))

