(ns zszj.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.reload :refer (wrap-reload)]
            [zszj.controllers.articles_controller :as articles]
            [zszj.controllers.article_types_controller :as article-types]
            [zszj.controllers.home_controller :as home]
            [zszj.controllers.materials_controller :as materials]
            [zszj.controllers.common :as common]
            [selmer.parser :as parser]))

(defn init
  []
  (parser/cache-off!)
  (println "web site is starting"))

(defn destroy
  []
  (println "web site is stopping"))

;;should change the state of is-paginate-for-notallempty maybe should write a macro to make it concise and elegant
(defroutes app-routes
  (GET "/" [] (do (if @common/is-paginate-for-notallempty
                    (swap! common/is-paginate-for-notallempty not))
                  (home/index)))
  (GET "/articles/:id" [id] (do (if @common/is-paginate-for-notallempty
                                  (swap! common/is-paginate-for-notallempty not))
                                (articles/render id)))
  (GET "/article_types/:id" [id page]
       (article-types/render id (do (if @common/is-paginate-for-notallempty
                                      (swap! common/is-paginate-for-notallempty not))
                                    (bigdec (if page page "1")))))
  (GET "/materials" [& args] (materials/index args))
  (GET "/materials/search" [& ajaxargs] (do (if (not @common/is-paginate-for-notallempty)
                                              (swap! common/is-paginate-for-notallempty not))
                                            (materials/search ajaxargs)))
  (GET "/materials/mini_search" [& ajaxargs] (do (if @common/is-paginate-for-notallempty
                                                   (swap! common/is-paginate-for-notallempty not))
                                                 (materials/mini_search ajaxargs)))
  ;;a demo for jquery ui datepicker
  (GET "/datepicker" [] (materials/datepicker))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-reload
   (handler/site app-routes)))
