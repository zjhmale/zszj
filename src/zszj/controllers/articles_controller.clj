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
        level2s (db/child-article-types root-id)
        level2s-with-level3s (map (fn [level2]
                                   (let [level3s (db/articles-of-type (:id level2))]
                                     (assoc level2 :level3s level3s))) level2s)]
    ;;(println "article id is: " id "\narticle: " article "\nroot-article-type: " root-article-type "\narticle-type: " article-type "\narticle-content: " (h/html-content (:content article)) "\nlevel2s: " level2s "\nlevel2-with-level3: " level2s-with-level3s)
    (layout/render "articles/show.html" {:article article
                                         :root-type root-article-type
                                         :level2s level2s-with-level3s})))

;;(parser/render "{% for item in test %}{% for elem in item %} {{elem.title}} {% endfor %}{% endfor %}" {:test [[{:title 123}] [{:title 321}] [{:title 333}]]})
;;(parser/render "{% for item in test %}{% for elem in item.level2 %} {{elem.title}} {% endfor %}{% endfor %}" {:test [{:level2 [{:title 321} {:title 333}]}]})

