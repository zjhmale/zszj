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

(defn get-home-articles-by-typekey
  [typekey limit-count truncate-length]
  (map (fn [article]
         (assoc
             (assoc article :truncate_title (helper/truncate_u (:title article) truncate-length))
           :addtime (helper/date-format (:addtime article))))
       (articles/home-article-by-typeid-and-limit
        (article-types/find-typeid-by-key typekey) limit-count)))

(defn index
  []
  (let [systemsiteid (links/find-linktypeid-by-subject "建设系统网站")
        othersiteid (links/find-linktypeid-by-subject "其他造价网站")
        systemsitelinks (links/find-links-by-linktypeid systemsiteid)
        othersitelinks (links/find-links-by-linktypeid othersiteid)
        home-softwares (map (fn [software]
                              (assoc software :truncate_title (helper/truncate_u (:title software) 12)))
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
        build-more-article-type (first (article-types/find-type "news" "jsdt"))
        jsdt-articles (get-home-articles-by-typekey "jsdt" 6 15)
        announce-more-article-type (first (article-types/find-type "news" "tzgg"))
        tzgg-articles (map (fn [article]
                             (if (re-find #"新发布标志" (:tags article))
                               (assoc article :new-publish true)
                               (assoc article :truncate_title-else (helper/truncate_u (:title article) 13))))
                           (get-home-articles-by-typekey "tzgg" 7 10))]
    ;;(println "systemsitelinks: " systemsitelinks "\nothersitelinks: " othersitelinks "\nmore-article-type: " build-more-article-type "\njsdt-articles: " jsdt-articles)
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
                    :build-more-article-type build-more-article-type
                    :jsdt-articles jsdt-articles
                    :announce-more-article-type announce-more-article-type
                    :tzgg-articles tzgg-articles
                    ;;for navibar
                    :menus layout/menus
                    :current-root-key "home"})))
