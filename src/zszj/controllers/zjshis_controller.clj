(ns zszj.controllers.zjshis_controller
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.controllers.common :as common]
            [zszj.controllers.zhongjies_controller :as zhongjies]
            [zszj.db.core :as db]
            [zszj.db.zjshis :as zjshis]
            [selmer.parser :as parser]))

(def PER-PAGE 20)

(defn generate-zjshis-html
  [zjshis]
  (reduce (fn [html zjshi]
            (str html "<tr onMouseOver=\\\"this.bgColor='#cbeceb';\\\" onmouseout=\\\"this.bgColor='#FFFFFF';\\\" bgcolor=\\\"#ffffff\\\"; bordercolor=\\\"#CCCCCC\\\" class=\\\"" (:odd-even zjshi) "\\\"><td width=\\\"40\\\" class=\\\"info\\\">" (:item-index zjshi) "</td><td width=\\\"90\\\" class=\\\"info\\\">" (:name zjshi) "</td><td width=\\\"280\\\" class=\\\"subject\\\">" (:company zjshi) "</td><td width=\\\"120\\\" class=\\\"info\\\">" (:zhengshu zjshi) "</td><td width=\\\"90\\\" class=\\\"info\\\">" (:zhuce zjshi) "</td><td width=\\\"90\\\" class=\\\"info\\\">" (:period zjshi) "</td></tr>")) "" zjshis))

(defn generate-main-html
  [zjshis latest-updatetime title]
  (let [zjshis-html (generate-zjshis-html zjshis)]
    (str "<div class=\\\"zizhi_remark\\\">本数据截止到" latest-updatetime "</div><table width=\\\"710\\\" height=\\\"25\\\" class=\\\"tableview\\\"><tr><th>序号</th><th>姓名</th><th>注册单位</th><th>注册证书号码</th><th>" title "</th><th>有效期</th></tr>" zjshis-html "</table>")))

(defn search
  [& ajaxargs]
  ;;(println ajaxargs)
  (if (not (-> (nth ajaxargs 0) :page))
    (let [param (nth ajaxargs 0)
          search_str (-> param :search_str)
          search_field (-> param :search :field)
          search_field (if (= search_field "zizhi_no") "zhengshu" search_field)
          current-page (-> param :page)
          current-page (bigdec (if current-page current-page "1"))
          latest-updatetime (clojure.string/split (nth (clojure.string/split (str (zjshis/get-latest-updatetime)) #" ") 0) #"-")
          latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")
          num-zjshis (if (not (empty? search_str))
                          (zjshis/zjshis-count-by-field search_field search_str)
                          (zjshis/get-zjshis-count))
          zjshis (if (not (empty? search_str))
                      (common/assoc-index-oddeven-with-paginator (zjshis/find-zjshis-by-field search_field search_str (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
                      (common/assoc-index-oddeven-with-paginator (zjshis/get-zjshis (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE))
          base-uri (str "/zjshis/search?_=1411317502456&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&search[field]=" search_field "&search_str=" search_str "&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=")
          zjshis-view (generate-main-html zjshis latest-updatetime "注册章号")
          paginator-view (common/generate-paginator-html num-zjshis base-uri current-page)]
      (str "jQuery(\"#view\").html(\"" zjshis-view paginator-view "\");\n"
           "jQuery(\"#view\").visualEffect(\"slide_down\") ;"))
    (let [subtitle (get zhongjies/title-map "zjshis")
          current-page (-> (nth ajaxargs 0) :page)
          current-page (bigdec (if current-page current-page "1"))
          search_str (-> (nth ajaxargs 0) :search_str)
          search_field (-> (nth ajaxargs 0) :search :field)
          search_field (if (= search_field "zizhi_no") "zhengshu" search_field)
          num-zjshis (if (not (empty? search_str))
                          (zjshis/zjshis-count-by-field search_field search_str)
                          (zjshis/get-zjshis-count))
          zjshis (if (not (empty? search_str))
                      (common/assoc-index-oddeven-with-paginator (zjshis/find-zjshis-by-field search_field search_str (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
                      (common/assoc-index-oddeven-with-paginator (zjshis/get-zjshis (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE))

          latest-updatetime (clojure.string/split (nth (clojure.string/split (str (zjshis/get-latest-updatetime)) #" ") 0) #"-")
          latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")]
      ;;(println "type: " type "\nsubtitle: " subtitle "\ncurrentpage: " current-page "\nzjshis: " zjshis "\nlatest-updatetime: " (str latest-updatetime))
      (layout/render "zhongjies/index.html"
                     (common/common-manipulate
                      (merge {:type "zjshis"
                              :subtitle subtitle
                              :latest-updatetime latest-updatetime
                              :zjshis zjshis
                              ;;for paginator
                              :current-page current-page
                              :current-page-dec (dec current-page)
                              :current-page-inc (inc current-page)
                              :num-articles num-zjshis}
                             (let [base-uri (str "/zjshis/search?_=1411317502456&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&search[field]=" search_field "&search_str=" search_str "&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=")]
                                   (common/paginator num-zjshis PER-PAGE current-page base-uri "notallempty"))) "zzzg")))))
