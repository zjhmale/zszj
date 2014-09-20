(ns zszj.controllers.zhongjies_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.db.books :as books]
            [zszj.views.helper :as helper]))

(defn index
  [& params]
  (layout/render "zhongjies/index.html"
                 (common/common-manipulate
                  {} "zzzg")))

(defn search
  [& ajaxargs]
  "cleantha")
