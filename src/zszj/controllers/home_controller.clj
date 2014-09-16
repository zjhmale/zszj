(ns zszj.controllers.home_controller
  (:use [korma.core :as kc])
  (:require [zszj.views.layout :as layout]
            [zszj.db.core :as db]
            [selmer.parser :as parser]
            [zszj.db.article-types :as article-types]
            [zszj.db.articles :as articles]
            [zszj.db.softwares :as softwares]
            [zszj.db.links :as links]))

(defn banner-notice []
  (-> "[横向公告]" articles/tagged select first))

(defn popup-notice []
  (-> "[弹出公告]" articles/tagged select first))

(defn index
  []
  (layout/render "home/index.html"
                 {:banner-notice (banner-notice)
                  :popup-notice (popup-notice)
                  ;;for navibar
                  :menus layout/menus
                  :current-root-key "home"}))
