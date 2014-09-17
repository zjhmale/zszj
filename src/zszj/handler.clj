(ns zszj.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.reload :refer (wrap-reload)]
            [zszj.controllers.articles_controller :as articles]
            [zszj.controllers.article_types_controller :as article-types]
            [zszj.controllers.home_controller :as home]
            [zszj.controllers.materials_controller :as materials]
            [selmer.parser :as parser]))

(defn init
  []
  (parser/cache-off!)
  (println "web site is starting"))

(defn destroy
  []
  (println "web site is stopping"))

(defroutes app-routes
  (GET "/" [] (home/index))
  (GET "/articles/:id" [id] (articles/render id))
  (GET "/article_types/:id" [id page]
       (article-types/render id (bigdec (if page page "1"))))
  (GET "/materials" [] (materials/index))
  (GET "/materials/search" [& ajaxargs] (materials/search ajaxargs))
  (GET "/materials/mini_search" [& ajaxargs] (materials/mini_search ajaxargs))
  ;;a demo for jquery ui datepicker
  (GET "/datepicker" [] (materials/datepicker))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-reload
   (handler/site app-routes)))
