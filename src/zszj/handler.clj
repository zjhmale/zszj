(ns zszj.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.reload :refer (wrap-reload)]
            [zszj.controllers.articles_controller :as articles]
            [zszj.controllers.article_types_controller :as article-types]
            [zszj.controllers.home_controller :as home]
            [zszj.controllers.materials_controller :as materials]
            [zszj.controllers.man_markets_controller :as man_markets]
            [zszj.controllers.man_costs_controller :as man_costs]
            [zszj.controllers.equipments_controller :as equipments]
            [zszj.controllers.tezbs_controller :as tezbs]
            [zszj.controllers.attachments_controller :as attachments]
            [zszj.controllers.softwares_controller :as softwares]
            [zszj.controllers.books_controller :as books]
            [zszj.controllers.zhongjies_controller :as zhongjies]
            [zszj.controllers.zhaobiaos_controller :as zhaobiaos]
            [zszj.controllers.zjshis_controller :as zjshis]
            [zszj.controllers.gaisuans_controller :as gaisuans]
            [zszj.controllers.common :as common]
            [zszj.controllers.search-controller :as search]
            [selmer.parser :as parser]))

(defn init
  []
  (parser/cache-off!)
  (println "web site is starting"))

(defn destroy
  []
  (println "web site is stopping"))

(defmacro turn-off-ajax-paginator-for-material
  [controller-action]
  `(do (if @common/is-paginate-for-notallempty
         (swap! common/is-paginate-for-notallempty not))
       ~controller-action))

;;(macroexpand '(turn-off-ajax-paginator-for-material (articles/render id)))

;;should change the state of is-paginate-for-notallempty maybe should write a macro to make it concise and elegant
(defroutes app-routes
           (GET "/" [] (do (if @common/is-paginate-for-notallempty
                             (swap! common/is-paginate-for-notallempty not))
                           (home/index)))
           (GET "/articles/:id" [id] (do (if @common/is-paginate-for-notallempty
                                           (swap! common/is-paginate-for-notallempty not))
                                         (articles/render id)))
           (GET "/article_types/:id" [id page]
             (turn-off-ajax-paginator-for-material (article-types/render id (bigdec (if page page "1")))))
           (GET "/materials" [& args] (materials/index args))
           (GET "/materials/search" [& ajaxargs] (do (if (not @common/is-paginate-for-notallempty)
                                                       (swap! common/is-paginate-for-notallempty not))
                                                     (materials/search ajaxargs)))
           (GET "/materials/mini_search" [& ajaxargs] (do (if @common/is-paginate-for-notallempty
                                                            (swap! common/is-paginate-for-notallempty not))
                                                          (materials/mini_search ajaxargs)))
           (GET "/man_markets" [] (turn-off-ajax-paginator-for-material (man_markets/index)))
           (GET "/man_markets/search" [& ajaxargs] (turn-off-ajax-paginator-for-material (man_markets/search ajaxargs)))
           (GET "/man_costs" [] (turn-off-ajax-paginator-for-material (man_costs/index)))
           (GET "/man_costs/search" [& ajaxargs] (turn-off-ajax-paginator-for-material (man_costs/search ajaxargs)))
           (GET "/equipments" [] (turn-off-ajax-paginator-for-material (equipments/index)))
           (GET "/equipments/search" [& ajaxargs] (turn-off-ajax-paginator-for-material (equipments/search ajaxargs)))
           ;;a demo for jquery ui datepicker
           (GET "/datepicker" [] (materials/datepicker))
           (GET "/gczjzbs" [page] (turn-off-ajax-paginator-for-material (tezbs/index (bigdec (if page page "1")))))
           (GET "/gczjzbs/:id" [id] (turn-off-ajax-paginator-for-material (tezbs/show id)))
           ;;the /attachments url is just a test for download static file
           (GET "/attachments" [] (attachments/index))
           (GET "/attachments/:id" [id] (attachments/send-file id))
           (GET "/softwares" [& params] (turn-off-ajax-paginator-for-material (softwares/index params)))
           (GET "/softwares/:id" [id] (turn-off-ajax-paginator-for-material (softwares/show id)))
           (GET "/books" [& params] (turn-off-ajax-paginator-for-material (books/index params)))
           (GET "/books/search" [& ajaxargs] (turn-off-ajax-paginator-for-material (books/search ajaxargs)))
           (GET "/zhongjies" [& params] (turn-off-ajax-paginator-for-material (zhongjies/index "zhongjies" params)))
           (GET "/zhaobiaos" [& params] (turn-off-ajax-paginator-for-material (zhongjies/index "zhaobiaos" params)))
           (GET "/zjshis" [& params] (turn-off-ajax-paginator-for-material (zhongjies/index "zjshis" params)))
           (GET "/gaisuans" [& params] (turn-off-ajax-paginator-for-material (zhongjies/index "gaisuans" params)))
           (GET "/zhongjies/search" [& ajaxargs] (zhongjies/search ajaxargs))
           (GET "/zhongjies/:id/info" [id & ajaxargs] (zhongjies/info id ajaxargs))
           (GET "/zhongjies/:id" [id] (zhongjies/show id))
           (GET "/zhaobiaos/search" [& ajaxargs] (zhaobiaos/search ajaxargs))
           (GET "/zhaobiaos/:id/info" [id & ajaxargs] (zhaobiaos/info id ajaxargs))
           (GET "/zhaobiaos/:id" [id] (zhaobiaos/show id))
           (GET "/zjshis/search" [& ajaxargs] (zjshis/search ajaxargs))
           (GET "/gaisuans/search" [& ajaxargs] (gaisuans/search ajaxargs))
           (GET "/search" [] (search/index))
           (GET "/search/search" [] (search/search))
           (route/resources "/")
           (route/not-found "Not Found"))

(def app
  (wrap-reload
    (handler/site app-routes)))
