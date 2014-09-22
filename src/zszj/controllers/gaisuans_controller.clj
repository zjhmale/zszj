(ns zszj.controllers.gaisuans_controller
  (:use [zszj.controllers.zhongjies_controller :only [PER-PAGE title-map]])
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.controllers.common :as common]
            [zszj.controllers.zhongjies_controller :as zhongjies]
            [zszj.db.core :as db]
            [zszj.db.gaisuans :as gaisuans]
            [zszj.db.zhaobiaos :as zhaobiaos]
            [selmer.parser :as parser]))

(defn generate-gaisuans-html
  [gaisuans]
  (reduce (fn [html gaisuan]
            (str html "<tr onMouseOver=\\\"this.bgColor='#cbeceb';\\\" onmouseout=\\\"this.bgColor='#FFFFFF';\\\" bgcolor=\\\"#ffffff\\\"; bordercolor=\\\"#CCCCCC\\\" class=\\\"" (:odd-even gaisuan) "\\\"><td width=\\\"40\\\" class=\\\"info\\\">" (:item-index gaisuan) "</td><td width=\\\"90\\\" class=\\\"info\\\">" (:name gaisuan) "</td><td width=\\\"280\\\" class=\\\"subject\\\">" (:danwei gaisuan) "</td><td width=\\\"120\\\"  class=\\\"info\\\">" (:zige gaisuan) "</td><td width=\\\"90\\\" class=\\\"info\\\">" (:zhye gaisuan) "</td><td width=\\\"90\\\"  class=\\\"info\\\">" (:period gaisuan) "</td></tr>")) "" gaisuans))

(defn generate-main-html
  [gaisuans latest-updatetime title]
  (let [gaisuans-html (generate-gaisuans-html gaisuans)]
    (str "<div class=\\\"zizhi_remark\\\">本数据截止到" latest-updatetime "</div><table width=\\\"710\\\" height=\\\"25\\\" class=\\\"tableview\\\"><tr><th>序号</th><th>姓名</th><th>注册单位</th><th>注册证书号码</th><th>" title "</th><th>有效期</th></tr>" gaisuans-html "</table>")))

(defn search
  [& ajaxargs]
  ;;(println ajaxargs)
  (if (not (-> (nth ajaxargs 0) :page))
    (let [param (nth ajaxargs 0)
          search_str (-> param :search_str)
          search_field (-> param :search :field)
          search_field (if (= search_field "zizhi_no") "zige" search_field)
          search_field (if (= search_field "company") "danwei" search_field)
          current-page (-> param :page)
          current-page (bigdec (if current-page current-page "1"))
          latest-updatetime (clojure.string/split (nth (clojure.string/split (str (gaisuans/get-latest-updatetime)) #" ") 0) #"-")
          latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")
          num-gaisuans (if (not (empty? search_str))
                          (gaisuans/gaisuans-count-by-field search_field search_str)
                          (gaisuans/get-gaisuans-count))
          gaisuans (if (not (empty? search_str))
                      (common/assoc-index-oddeven-with-paginator (gaisuans/find-gaisuans-by-field search_field search_str (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
                      (common/assoc-index-oddeven-with-paginator (gaisuans/get-gaisuans (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE))
          base-uri (str "/gaisuans/search?_=1411347564022&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&search[field]=" search_field "&search_str=" search_str "&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=")
          gaisuans-view (generate-main-html gaisuans latest-updatetime "专业")
          paginator-view (common/generate-paginator-html num-gaisuans base-uri current-page)]
      (str "jQuery(\"#view\").html(\"" gaisuans-view paginator-view "\");\n"
           "jQuery(\"#view\").visualEffect(\"slide_down\");"))
    (let [subtitle (get title-map "gaisuans")
          current-page (-> (nth ajaxargs 0) :page)
          current-page (bigdec (if current-page current-page "1"))
          search_str (-> (nth ajaxargs 0) :search_str)
          search_field (-> (nth ajaxargs 0) :search :field)
          search_field (if (= search_field "zizhi_no") "zige" search_field)
          search_field (if (= search_field "company") "danwei" search_field)
          num-gaisuans (if (not (empty? search_str))
                         (gaisuans/gaisuans-count-by-field search_field search_str)
                         (gaisuans/get-gaisuans-count))
          gaisuans (if (not (empty? search_str))
                     (common/assoc-index-oddeven-with-paginator (gaisuans/find-gaisuans-by-field search_field search_str (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE)
                     (common/assoc-index-oddeven-with-paginator (gaisuans/get-gaisuans (* (dec current-page) PER-PAGE) PER-PAGE) (int current-page) PER-PAGE))
          latest-updatetime (clojure.string/split (nth (clojure.string/split (str (gaisuans/get-latest-updatetime)) #" ") 0) #"-")
          latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")]
      ;;(println "type: " type "\nsubtitle: " subtitle "\ncurrentpage: " current-page "\ngaisuans: " gaisuans "\nlatest-updatetime: " (str latest-updatetime))
      (layout/render "zhongjies/index.html"
                     (common/common-manipulate
                      (merge {:type "gaisuans"
                              :subtitle subtitle
                              :latest-updatetime latest-updatetime
                              :gaisuans gaisuans
                              ;;for paginator
                              :current-page current-page
                              :current-page-dec (dec current-page)
                              :current-page-inc (inc current-page)
                              :num-articles num-gaisuans}
                             (let [ base-uri (str "/gaisuans/search?_=1411347564022&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&search[field]=" search_field "&search_str=" search_str "&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=")]
                               (common/paginator num-gaisuans PER-PAGE current-page base-uri "notallempty"))) "zzzg")))))
