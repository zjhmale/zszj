(ns zszj.controllers.articles_controller
  (:require [zszj.layout :as layout]
            [zszj.db.core :as db]
            [selmer.parser :as parser]))

(defn render
  [id]
  (let [article  (db/article id)
        article-type-id (:article_type_id article)
        article-type (db/article-type article-type-id)
        root-article-type (db/root-article-type article-type-id)
        parent-article-type (db/parent-article-type article-type-id)
        root-id (:id root-article-type)
        current-root-key (:key root-article-type)
        level2s (db/child-article-types root-id)
        level2s-with-level3s-and-articles
        ;;level3 is the child article-type of level2 and articles is many-to one with level2
        (map (fn [level2]
               (let [level2id (:id level2)
                     level3s (db/child-article-types level2id)
                     articles (db/articles-of-type level2id)]
                 (assoc (assoc level2 :level3s level3s) :articles articles))) level2s)]
    ;;(println "article id is: " id "\narticle: " article "\nroot-article-type: " root-article-type "\narticle-type: " article-type "\nlevel2s: " level2s "\nlevel2-with-level3-and-articles: " level2s-with-level3s-and-articles "\nroot-key: " current-root-key "\nmenus: " layout/menus)
    (layout/render "articles/show.html"
                   {:article article
                    :article-type article-type
                    :root-type root-article-type
                    :parent-type parent-article-type
                    ;;for navigator
                    :level2s level2s-with-level3s-and-articles
                    ;;for navibar
                    :menus layout/menus
                    :current-root-key current-root-key})))

;;(parser/render "{% for item in test %}{% for elem in item %} {{elem.title}} {% endfor %}{% endfor %}" {:test [[{:title 123}] [{:title 321}] [{:title 333}]]})
;;(parser/render "{% for item in test %}{% for elem in item.level2 %} {{elem.title}} {% endfor %}{% endfor %}" {:test [{:level2 [{:title 321} {:title 333}]}]})
;;(parser/render "{% ifequal ttt \"cleantha\" %}true{% endifequal %}" {:ttt "cleantha"})


