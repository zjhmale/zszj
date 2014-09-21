(ns zszj.controllers.zhongjies_controller
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.db.zhongjies :as zhongjies]
            [zszj.db.zhaobiaos :as zhaobiaos]
            [zszj.db.zjshis :as zjshis]
            [zszj.db.gaisuans :as gaisuans]
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
  ;;(println type)
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
                            (common/paginator num-zjshis PER-PAGE current-page "/zjshis")) "zzzg")))
   (= type "gaisuans")
   (let [subtitle (get title-map type)
         current-page (-> (nth params 0) :page)
         current-page (bigdec (if current-page current-page "1"))
         gaisuans (common/assoc-index-oddeven-with-paginator (gaisuans/get-gaisuans (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
         num-gaisuans (gaisuans/get-gaisuans-count)
         latest-updatetime (clojure.string/split (nth (clojure.string/split (str (gaisuans/get-latest-updatetime)) #" ") 0) #"-")
         latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")]
     ;;(println "type: " type "\nsubtitle: " subtitle "\ncurrentpage: " current-page "\ngaisuans: " gaisuans "\nlatest-updatetime: " (str latest-updatetime))
     (layout/render "zhongjies/index.html"
                    (common/common-manipulate
                     (merge {:type type
                             :subtitle subtitle
                             :latest-updatetime latest-updatetime
                             :gaisuans gaisuans
                             ;;for paginator
                             :current-page current-page
                             :current-page-dec (dec current-page)
                             :current-page-inc (inc current-page)
                             :num-articles num-gaisuans}
                            (common/paginator num-gaisuans PER-PAGE current-page "/gaisuans")) "zzzg")))))

(defn- view-from-template
  [zhongjie]
  (parser/render (slurp "resources/views/zhongjies/info.html")
                 {:zhongjie zhongjie}))

(defn- generate-zhongjies-html
  [zhongjies]
  (reduce (fn [html zhongjie]
            (str html "<tr onMouseOver=\\\"this.bgColor='#cbeceb';\\\" onmouseout=\\\"this.bgColor='#FFFFFF';\\\" bgcolor=\\\"#ffffff\\\"; bordercolor=\\\"#CCCCCC\\\" class=\\\"" (:odd-even zhongjie) "\\\"><td width=\\\"40\\\" class=\\\"info\\\">" (:item-index zhongjie) "</td><td width=\\\"220\\\" class=\\\"subject\\\"><a href=\\\"/zhongjies/" (:id zhongjie) "\\\" class=\\\"or sticky\\\" id=\\\"" (:id zhongjie) "\\\" name=\\\"" (:company zhongjie) "\\\" rel=\\\"/zhongjies/" (:id zhongjie) "/info\\\" title=\\\"\\\"><img alt=\\\"View-fullscreen\\\" src=\\\"/qx/icon/Tango/16/actions/view-fullscreen.png?1374630107\\\"></a><a href=\\\"/zhongjies/" (:id zhongjie) "\\\" class=\\\"or\\\" id=\\\"" (:id zhongjie) "\\\" name=\\\"" (:company zhongjie) "\\\" rel=\\\"/zhongjies/" (:id zhongjie) "\\\">" (:company zhongjie) "</a></td><td width=\\\"110\\\" class=\\\"info\\\">" (:zizhi_no zhongjie) "</td><td width=\\\"60\\\" class=\\\"info\\\">" (:faren zhongjie) "</td><td width=\\\"60\\\" class=\\\"info\\\">" (:grade zhongjie) "</td><td width=\\\"220\\\" class=\\\"info\\\">" (:period zhongjie)"</td></tr>")) "" zhongjies))

(defn generate-main-html
  [zhongjies latest-updatetime]
  (let [zhongjies-html (generate-zhongjies-html zhongjies)]
    (str "<script type=\\\"text/javascript\\\">$(document).ready(function() {$('.sticky').cluetip({sticky: false, closePosition: 'title', arrows: true, width:480}) ;});</script><div class=\\\"zizhi_remark\\\">本数据截止到 " latest-updatetime "</div><table width=\\\"710\\\" height=\\\"25\\\" class=\\\"tableview\\\"><tr><th>序号</th><th>单位名称</th><th>证书编号</th><th>法人代表</th><th>资质等级</th><th>有效期</th></tr>" zhongjies-html "</table>")))

(defn search
  [& ajaxargs]
  (println ajaxargs)
  (if (not (-> (nth ajaxargs 0) :page))
    (let [param (nth ajaxargs 0)
          search_str (-> param :search_str)
          search_field (-> param :search :field)
          current-page (-> param :page)
          current-page (bigdec (if current-page current-page "1"))
          latest-updatetime (clojure.string/split (nth (clojure.string/split (str (zhongjies/get-latest-updatetime)) #" ") 0) #"-")
          latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")
          num-zhongjies (if (not (empty? search_str))
                          (zhongjies/zhongjies-count-by-field search_field search_str)
                          (zhongjies/get-zhongjies-count))
          zhongjies (if (not (empty? search_str))
                      (common/assoc-index-oddeven-with-paginator (zhongjies/find-zhongjies-by-field search_field search_str (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
                      (common/assoc-index-oddeven-with-paginator (zhongjies/get-zhongjies (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE))
          base-uri (str "/zhongjies/search?_=1411299918115&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&search[field]=" search_field "&search_str=" search_str)
          zhongjies-view (generate-main-html zhongjies latest-updatetime)
          paginator-view (common/generate-paginator-html num-zhongjies base-uri current-page)]
      (println "search_str: " search_str "\nsearch_field: " (keyword search_field))
      (str "jQuery(\"#view\").html(\"" zhongjies-view paginator-view "\");\n"
           "jQuery(\"#view\").visualEffect(\"slide_down\");"))
    (let [subtitle (get title-map "zhongjies")
          current-page (-> (nth ajaxargs 0) :page)
          current-page (bigdec (if current-page current-page "1"))
          search_str (-> (nth ajaxargs 0) :search_str)
          search_field (-> (nth ajaxargs 0) :search :field)
          zhongjies (common/assoc-index-oddeven-with-paginator (zhongjies/get-zhongjies (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
          num-zhongjies (zhongjies/get-zhongjies-count)
          latest-updatetime (clojure.string/split (nth (clojure.string/split (str (zhongjies/get-latest-updatetime)) #" ") 0) #"-")
          latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")]
      ;;(println "current-page: " current-page "\nsearch_str: " search_str "\nsearch_field: " search_field "\n zhongjies: " zhongjies)
      (layout/render "zhongjies/index.html"
                     (common/common-manipulate
                      (merge {:type "zhongjies"
                              :subtitle subtitle
                              :latest-updatetime latest-updatetime
                              :zhongjies zhongjies
                              ;;for paginator
                              :current-page current-page
                              :current-page-dec (dec current-page)
                              :current-page-inc (inc current-page)
                              :num-articles num-zhongjies}
                             (let [base-uri (str "/zhongjies/search?_=1411299918115&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&search[field]=" search_field "&search_str=" search_str)]
                               (common/paginator num-zhongjies PER-PAGE current-page base-uri "notallempty"))) "zzzg")))))

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
