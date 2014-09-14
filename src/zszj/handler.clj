(ns zszj.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.reload :refer (wrap-reload)]
            [zszj.controllers.articles_controller :as articles]))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/articles/:id" [id] (articles/articles-render id))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-reload
   (handler/site app-routes)))
