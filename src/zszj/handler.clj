(ns zszj.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.reload :refer (wrap-reload)]
            [zszj.controllers.articles_controller :as articles]
            [zszj.controllers.article_types_controller :as article-types]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/articles/:id" [id] (articles/render id))
  (GET "/article_types/:id" [id page]
       (article-types/render id (bigdec (if page page "1"))))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-reload
   (handler/site app-routes)))
