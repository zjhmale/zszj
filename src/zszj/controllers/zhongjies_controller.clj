(ns zszj.controllers.zhongjies_controller
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.db.zhongjies :as zhongjies]
            [zszj.db.zhaobiaos :as zhaobiaos]
            [zszj.db.zjshis :as zjshis]
            [selmer.parser :as parser]))

(def title-map
  {"zhongjies" "造价咨询企业"
   "zhaobiaos" "招标代理机构"
   "zjshis" "造价工程师"
   "gaisuans" "造价员"})

(def ^:dynamic *PER-PAGE* 20)

(def PER-PAGE 20)

(defn index
  [type & params]
  (cond
   (= type "zhongjies")
   (let [subtitle (get title-map type)
         current-page (-> (nth params 0) :page)
         current-page (bigdec (if current-page current-page "1"))
         zhongjies (common/assoc-index-oddeven-with-paginator (zhongjies/get-zhongjies (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
         num-zhongjies (zhongjies/get-zhongjies-count)
         latest-updatetime (clojure.string/split (nth (clojure.string/split (str (zhongjies/get-latest-updatetime)) #" ") 0) #"-")
         latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")]
     ;;(println "type: " type "\nsubtitle: " subtitle "\ncurrentpage: " current-page "\nzhongjies: " zhongjies "\nlatest-updatetime: " (str latest-updatetime))
     (layout/render "zhongjies/index.html"
                    (common/common-manipulate
                     (merge {:type type
                             :subtitle subtitle
                             :latest-updatetime latest-updatetime
                             :zhongjies zhongjies
                             ;;for paginator
                             :current-page current-page
                             :current-page-dec (dec current-page)
                             :current-page-inc (inc current-page)
                             :num-articles num-zhongjies}
                            (common/paginator num-zhongjies PER-PAGE current-page "/zhongjies")) "zzzg")))
   (= type "zhaobiaos")
   (let [subtitle (get title-map type)
         current-page (-> (nth params 0) :page)
         current-page (bigdec (if current-page current-page "1"))
         zhaobiaos (common/assoc-index-oddeven-with-paginator (zhaobiaos/get-zhaobiaos (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
         num-zhaobiaos (zhaobiaos/get-zhaobiaos-count)
         latest-updatetime (clojure.string/split (nth (clojure.string/split (str (zhaobiaos/get-latest-updatetime)) #" ") 0) #"-")
         latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")]
     ;;(println "type: " type "\nsubtitle: " subtitle "\ncurrentpage: " current-page "\nzhaobiaos: " zhaobiaos "\nlatest-updatetime: " (str latest-updatetime))
     (layout/render "zhongjies/index.html"
                    (common/common-manipulate
                     (merge {:type type
                             :subtitle subtitle
                             :latest-updatetime latest-updatetime
                             :zhaobiaos zhaobiaos
                             ;;for paginator
                             :current-page current-page
                             :current-page-dec (dec current-page)
                             :current-page-inc (inc current-page)
                             :num-articles num-zhaobiaos}
                            (common/paginator num-zhaobiaos PER-PAGE current-page "/zhaobiaos")) "zzzg")))
   (= type "zjshis")
   (let [subtitle (get title-map type)
         current-page (-> (nth params 0) :page)
         current-page (bigdec (if current-page current-page "1"))
         zjshis (common/assoc-index-oddeven-with-paginator (zjshis/get-zjshis (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
         num-zjshis (zjshis/get-zjshis-count)
         latest-updatetime (clojure.string/split (nth (clojure.string/split (str (zjshis/get-latest-updatetime)) #" ") 0) #"-")
         latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")]
     ;;(println "type: " type "\nsubtitle: " subtitle "\ncurrentpage: " current-page "\nzjshis: " zjshis "\nlatest-updatetime: " (str latest-updatetime))
     (layout/render "zhongjies/index.html"
                    (common/common-manipulate
                     (merge {:type type
                             :subtitle subtitle
                             :latest-updatetime latest-updatetime
                             :zjshis zjshis
                             ;;for paginator
                             :current-page current-page
                             :current-page-dec (dec current-page)
                             :current-page-inc (inc current-page)
                             :num-articles num-zjshis}
                            (common/paginator num-zjshis PER-PAGE current-page "/zjshis")) "zzzg")))))

(defn- view-from-template
  [zhongjie]
  (parser/render (slurp "resources/views/zhongjies/info.html")
                 {:zhongjie zhongjie}))

(defn search
  [& ajaxargs]
  (str "jQuery(\"#view\").visualEffect(\"slide_down\");"))

(defn show
  [id]
  (let [zhongjie (zhongjies/get-zhongjie-by-id id)]
    (layout/render "zhongjies/show.html"
                   (common/common-manipulate
                    {:zhongjie zhongjie} "zzzg"))))

(defn info
  [id ajaxargs]
  ;;(println id)
  ;;(println ajaxargs)
  (let [zhongjie (zhongjies/get-zhongjie-by-id id)]
    (view-from-template zhongjie)))
