(ns zszj.controllers.home_controller
  (:use [korma.core :as kc])
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.db.core :as db]
            [selmer.parser :as parser]
            [zszj.db.article-types :as article-types]
            [zszj.db.articles :as articles]
            [zszj.db.softwares :as softwares]
            [zszj.db.links :as links]
            [zszj.db.attachments :as attachments]))

(defn banner-notice []
  (-> "[横向公告]" articles/tagged select first))

(defn popup-notice []
  (-> "[弹出公告]" articles/tagged select first))

(def focus_width 225)
(def focus_height 145)
(def text_height 20)

(defn index
  []
  (let [systemsiteid (links/find-linktypeid-by-subject "建设系统网站")
        othersiteid (links/find-linktypeid-by-subject "其他造价网站")
        systemsitelinks (links/find-links-by-linktypeid systemsiteid)
        othersitelinks (links/find-links-by-linktypeid othersiteid)
        home-softwares (map (fn [software]
                              (assoc software :title (helper/truncate_u (:title software) 12)))
                            (softwares/home-softwares))
        swf_height (+ focus_height text_height)
        headlines (articles/find-articles-by-tag "图文公告")
        pics (clojure.string/join
              "|"
              (map (fn [headline]
                     (let [attachment (attachments/find-first-attachment-by-articleid (:id headline))]
                       (if attachment
                         (str "/attachments/" (:id attachment))
                         (:pic headline))))
                   headlines))
        links (clojure.string/join "|"
                                   (map (fn [headline]
                                          (str "/articles/" (:id headline)))
                                        headlines))
        texts (clojure.string/join "|"
                                   (map (fn [headline]
                                          (helper/truncate (:title headline) 19))
                                        headlines))
        more-article-type (first (article-types/find_type "news" "jsdt"))]
    (println "systemsitelinks: " systemsitelinks "\nothersitelinks: " othersitelinks "\nmore-article-type: " more-article-type)
    (layout/render "home/index.html"
                   {:banner-notice (banner-notice)
                    :popup-notice (popup-notice)
                    :home-softwares home-softwares
                    :systemsitelinks systemsitelinks
                    :othersitelinks othersitelinks
                    :focus_width focus_width
                    :focus_height focus_height
                    :text_height text_height
                    :swf_height swf_height
                    :headlines headlines
                    :pics pics
                    :links links
                    :texts texts
                    :more-article-type more-article-type
                    ;;for navibar
                    :menus layout/menus
                    :current-root-key "home"})))
