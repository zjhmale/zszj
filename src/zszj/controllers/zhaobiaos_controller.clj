(ns zszj.controllers.zhaobiaos_controller
  (:use [zszj.controllers.zhongjies_controller :only [PER-PAGE]])
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.controllers.common :as common]
            [zszj.controllers.zhongjies_controller :as zhongjies]
            [zszj.db.core :as db]
            [zszj.db.zhaobiaos :as zhaobiaos]
            [selmer.parser :as parser]))

;;(def PER-PAGE 20)

(defn- view-from-template
  [zhaobiao]
  (parser/render (slurp "resources/views/zhaobiaos/info.html")
                 {:zhaobiao zhaobiao}))

(defn search
  [& ajaxargs]
  ;;(println ajaxargs)
  (if (not (-> (nth ajaxargs 0) :page))
    (let [param (nth ajaxargs 0)
          search_str (-> param :search_str)
          search_field (-> param :search :field)
          current-page (-> param :page)
          current-page (bigdec (if current-page current-page "1"))
          latest-updatetime (clojure.string/split (nth (clojure.string/split (str (zhaobiaos/get-latest-updatetime)) #" ") 0) #"-")
          latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")
          num-zhaobiaos (if (not (empty? search_str))
                          (zhaobiaos/zhaobiaos-count-by-field search_field search_str)
                          (zhaobiaos/get-zhaobiaos-count))
          zhaobiaos (if (not (empty? search_str))
                      (common/assoc-index-oddeven-with-paginator (zhaobiaos/find-zhaobiaos-by-field search_field search_str (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
                      (common/assoc-index-oddeven-with-paginator (zhaobiaos/get-zhaobiaos (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE))
          base-uri (str "/zhaobiaos/search?_=1411304244920&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&search[field]=" search_field "&search_str=" search_str)
          zhaobiaos-view (zhongjies/generate-main-html zhaobiaos latest-updatetime)
          paginator-view (common/generate-paginator-html num-zhaobiaos base-uri current-page)]
      (str "jQuery(\"#view\").html(\"" zhaobiaos-view paginator-view "\");\n"
           "jQuery(\"#view\").visualEffect(\"slide_down\");"))
    (let [subtitle (get zhongjies/title-map "zhaobiaos")
          current-page (-> (nth ajaxargs 0) :page)
          current-page (bigdec (if current-page current-page "1"))
          search_str (-> (nth ajaxargs 0) :search_str)
          search_field (-> (nth ajaxargs 0) :search :field)
          num-zhaobiaos (if (not (empty? search_str))
                          (zhaobiaos/zhaobiaos-count-by-field search_field search_str)
                          (zhaobiaos/get-zhaobiaos-count))
          zhaobiaos (if (not (empty? search_str))
                      (common/assoc-index-oddeven-with-paginator (zhaobiaos/find-zhaobiaos-by-field search_field search_str (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
                      (common/assoc-index-oddeven-with-paginator (zhaobiaos/get-zhaobiaos (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE))
          latest-updatetime (clojure.string/split (nth (clojure.string/split (str (zhaobiaos/get-latest-updatetime)) #" ") 0) #"-")
          latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")]
      ;;(println "type: " type "\nsubtitle: " subtitle "\ncurrentpage: " current-page "\nzhaobiaos: " zhaobiaos "\nlatest-updatetime: " (str latest-updatetime))
      (layout/render "zhongjies/index.html"
                     (common/common-manipulate
                      (merge {:type "zhaobiaos"
                              :subtitle subtitle
                              :latest-updatetime latest-updatetime
                              :zhaobiaos zhaobiaos
                              ;;for paginator
                              :current-page current-page
                              :current-page-dec (dec current-page)
                              :current-page-inc (inc current-page)
                              :num-articles num-zhaobiaos}
                             (let [base-uri (str "/zhaobiaos/search?_=1411304244920&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&search[field]=" search_field "&search_str=" search_str)]
                               (common/paginator num-zhaobiaos PER-PAGE current-page base-uri "notallempty"))) "zzzg")))))

(defn show
  [id]
  (let [zhaobiao (zhaobiaos/get-zhaobiao-by-id id)]
    (layout/render "zhaobiaos/show.html"
                   (common/common-manipulate
                    {:zhaobiao zhaobiao} "zzzg"))))

(defn info
  [id ajaxargs]
  ;;(println id)
  ;;(println ajaxargs)
  (let [zhaobiao (zhaobiaos/get-zhaobiao-by-id id)]
    (view-from-template zhaobiao)))
