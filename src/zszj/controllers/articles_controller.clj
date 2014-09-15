(ns zszj.controllers.articles_controller
  (:require [zszj.layout :as layout]
            [zszj.db.core :as db]
            [net.cgrand.enlive-html :as h]
            [selmer.parser :as parser]))

(defn articles-render
  [id]
  (let [article  (db/article id)
        article-type-id (:article_type_id article)
        article-type (db/article-type article-type-id)
        root-article-type (db/root-article-type article-type-id)
        root-id (:id root-article-type)
        level2s (db/child-article-types root-id)]
    ;;(println "article id is: " id "\narticle: " article "\nroot-article-type: " root-article-type "\narticle-type: " article-type "\narticle-content: " (h/html-content (:content article)) "\nlevel2s: " level2s)
    (layout/render "articles/show.html" {:article article
                                         :root-type root-article-type
                                         :level2s level2s})))
