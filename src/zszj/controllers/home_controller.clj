(ns zszj.controllers.home_controller
  (:use [korma.core :as kc])
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
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
  (let [systemsiteid (links/find-linktypeid-by-subject "建设系统网站")
        othersiteid (links/find-linktypeid-by-subject "其他造价网站")
        systemsitelinks (links/find-links-by-linktypeid systemsiteid)
        othersitelinks (links/find-links-by-linktypeid othersiteid)
        home-softwares (map (fn [software]
                              (assoc software :title (helper/truncate_u (:title software) 12)))
                            (softwares/home-softwares))]
    ;;(println "systemsitelinks: " systemsitelinks "\nothersitelinks: " othersitelinks)
    (layout/render "home/index.html"
                   {:banner-notice (banner-notice)
                    :popup-notice (popup-notice)
                    :home-softwares home-softwares
                    :systemsitelinks systemsitelinks
                    :othersitelinks othersitelinks
                    ;;for navibar
                    :menus layout/menus
                    :current-root-key "home"})))
