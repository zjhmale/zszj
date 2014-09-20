(ns zszj.views.layout
  (:require [selmer.parser :as parser]
            [clojure.string :as s]
            [ring.util.response :refer [content-type response]]
            [compojure.response :refer [Renderable]]
            [environ.core :refer [env]]
            [zszj.db.core :as db]))

(def template-path "views/")

(deftype RenderableTemplate [template params]
  Renderable
  (render [this request]
    (content-type
      (->> (assoc params
                  (keyword (s/replace template #".html" "-selected")) "active"
                  :dev (env :dev)
                  :servlet-context
                  (if-let [context (:servlet-context request)]
                    ;; If we're not inside a serlvet environment (for
                    ;; example when using mock requests), then
                    ;; .getContextPath might not exist
                    (try (.getContextPath context)
                         (catch IllegalArgumentException _ context))))
        (parser/render-file (str template-path template))
        response)
      "text/html; charset=utf-8")))

(defn render [template & [params]]
  (RenderableTemplate. template params))

(defn to-article-type [key]
  (let [type (db/article-type-by-key key)] 
    (str "/article_types/" (:id type))))

(def menus [{:key "home" :title "首页" :href "/"}
            {:key "zwgk" :title "政务公开" :href (to-article-type "zwgk")}
            {:key "news" :title "新闻中心" :href (to-article-type "news")}
            {:key "bszn" :title "办事指南" :href (to-article-type "bszn")}
            {:key "policy" :title "政策法规" :href (to-article-type "policy")}
            {:key "jgxx" :title "价格信息" :href "/man_markets"}
            {:key "price_area" :title "计价天地" :href (to-article-type "price_area")}
            {:key "zzzg", :title "资质资格" :href "/zhongjies"},
            {:key "public_info", :title "公示信息" :href (to-article-type "sjgs_public")}
            {:key "cost_association", :title "造价协会" :href (to-article-type "cost_association")}
            {:key "zlxz", :title "资料下载" :href "/softwares?software_type=资质管理"}
            {:key "wssx", :title "网上书讯" :href "/books?book_type=国家标准规范"}])
