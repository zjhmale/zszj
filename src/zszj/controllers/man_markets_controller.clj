(ns zszj.controllers.man_markets_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.man_markets :as man_markets]
            [selmer.parser :as parser]))

(def season-map
  {:1 "一"
   :2 "二"
   :3 "三"
   :4 "四"})

(defn assoc-index-oddeven
  [collections]
  (map (fn [item]
         (let [item-index (inc (.indexOf (vec collections) item))
               odd-even (if (odd? item-index)
                          "odd"
                          "even")]
           (assoc (assoc item :odd-even odd-even) :item-index item-index)))
       collections))

(defn index
  []
  (let [all-year-seasons (sort-by :year >
                          (sort-by :season >
                                         (map (fn [pair]
                                                (assoc (assoc (assoc pair :chinese-season (get season-map (keyword (:season pair)))) :year (Integer/parseInt (:year pair))) :season (Integer/parseInt (:season pair))))
                                              (man_markets/get-year-season))))
        latest-year-season (man_markets/get-latest-year-season)
        year (:year latest-year-season)
        season (:season latest-year-season)
        items (man_markets/find-all-by-year-and-season year season)
        items-for2010-raw (man_markets/find-all-by-year-and-season-and-version year season "2010")
        items (clojure.set/difference items items-for2010-raw)
        items-for2010 (assoc-index-oddeven items-for2010-raw)
        items (assoc-index-oddeven items)
        is-show-price-title (and (= "2011" year) (= "3" season))
        is-show-remark-5 (or (= "2012" year) (and (= "2011" year) (= "4" season)))]
    ;;(println "all-year-seasons: " all-year-seasons "\nlatest-year-season: " latest-year-season "\nitems: " items "\nitems-for2010: " items-for2010 "\nyear class: " (class year) "\nseason class: " (class season))
    (layout/render "man_markets/index.html"
                   (common/common-manipulate {:all-year-seasons all-year-seasons
                                              :items items
                                              :items-for2010 items-for2010
                                              :is-show-remark-5 is-show-remark-5
                                              :is-show-price-title is-show-price-title
                                              :is-show-items-for2010 (not (empty? items-for2010))
                                              :is-show-items (not (empty? items))} "jgxx"))))

;;(clojure.set/difference #{{:three 3} {:two 2} {:one 1}} #{{:one 1}})

(defn generate-item-html
  [collections]
  (reduce (fn [html item]
            (str html "<tr onMouseOver=\\\"this.bgColor='#cbeceb'\\\" onmouseout=\\\"this.bgColor='#FFFFFF'\\\" bgcolor=\\\"#ffffff\\\"; class=\\\"" (:odd-even item) "\\\"><td width=\\\"40\\\" class=\\\"info\\\">" (:item-index item) "</td><td width=\\\"180\\\" class=\\\"info\\\">" (:man_type item) "</td><td width=\\\"180\\\" class=\\\"info\\\">" (:unit item) "</td><td width=\\\"180\\\"  class=\\\"info\\\">" (:price item) "<br /></td></tr>")) "" collections))

(defn- create-item-html
  [items year season is-show-price-title]
  (if (not (empty? items))
    (let [items-html (generate-item-html items)]
      (str "<div class=\\\"price_title\\\">" year "年第" season "季度舟山市建设工程人工市场信息价" (if is-show-price-title (str "(适用2003版计价依据)") (str "")) "</div><table width=\\\"710\\\" height=\\\"25\\\" class=\\\"tableview\\\"><tr><th>序号</th><th>人工类别</th><th>单 位</th><th>信息价(元/工日)</th></tr>" items-html "</table><div class=\\\"price_remark\\\">1、人工市场信息价可作为按照本省 2003 版计价依据编制工程概算、预算、招标控制价、投标报价、竣工结算时计算建设工程人工费的指导性依据。</div><div class=\\\"price_remark\\\">2、人工市场信息价作为计算人工费的依据，应计入直接费中，并作为取费基数</div>"))
    (str "")))

(defn- view
  [items-for2010 items year season is-show-price-title is-show-remark-5]
  (if (not (empty? items-for2010))
    (do
      (let [items-for2010-html (generate-item-html items-for2010)]
        (str "<br/><div class=\\\"price_title\\\">" year "年第" season "季度舟山市建设工程人工市场信息价" (if is-show-price-title (str "(适用2010版计价依据)") (str "")) "</div><table width=\\\"710\\\" height=\\\"25\\\" class=\\\"tableview\\\"><tr><th>序号</th><th>人工类别</th><th>单 位</th><th>信息价(元/工日)</th></tr>" items-for2010-html "</table><div class=\\\"price_remark\\\">1、人工市场信息价可作为按照本省2010 版计价依据编制工程概算、预算、招标控制价、投标报价、竣工结算时计算建设工程人工费的指导性依据。</div><div class=\\\"price_remark\\\">2、编制招标控制价时人工市场信息价不作为计算费用的基数，仅计取税金。</div><div class=\\\"price_remark\\\">3、机上人工费执行人工市场信息价。</div><div class=\\\"price_remark\\\">4、本人工市场信息价自2011 年第3 季度起发布执行。本人工市场信息价2011 年10 月份信息价发布之日前已发布招标文件或签订施工合同的建设项目，在计算人工费风险范围外调整价差时，投标报价文件编制期对应的人工市场信息价采用浙江省2010 版计价依据定额人工基价，计算合同期人工市场信息价平均值时，2011 年1-6 月份采用浙江省2010 版计价依据的定额人工基价，2011 年7 月份起采用市建设工程造价管理机构发布的人工市场信息价。</div>" (if is-show-remark-5 (str "<div class=\\\"price_remark\\\">5、按照本省2003 版计价依据计算人工费时，本人工市场信息价乘以系数0.93（数值取整，小数点四舍五入）。</div>") (str "")) "<br/><br/>")))
    (if (not (empty? items))
      (let [items-html (create-item-html items year season is-show-price-title)]
        items-html)
      (str "没有找到相关记录"))))

;;deprecated
;;the idea that generate dynamic refresh view from template file is faild cause it seems that the dynamic refresh view must be in a straight line
(defn- view-from-template
  [items-for2010 items year season is-show-price-title is-show-remark-5]
  (parser/render (slurp "resources/views/man_markets/view_refresh.html")
                 {:items-for2010 items-for2010
                  :items items
                  :year year
                  :season season
                  :is-show-price-title is-show-price-title
                  :is-show-remark-5 is-show-remark-5}))

(defn search
  [& ajaxargs]
  ;;(println ajaxargs)
  (let [params (clojure.string/split (-> (nth ajaxargs 0) :selected :season) #"-")
        year (nth params 0)
        season (nth params 1)
        items (man_markets/find-all-by-year-and-season year season)
        items-for2010 (man_markets/find-all-by-year-and-season-and-version year season "2010")
        items (clojure.set/difference items items-for2010)
        items (assoc-index-oddeven items)
        items-for2010 (assoc-index-oddeven items-for2010)
        is-show-price-title (and (= "2011" year) (= "3" season))
        is-show-remark-5 (or (= "2012" year) (and (= "2011" year) (= "4" season)))
        view-html (view items-for2010 items year season is-show-price-title is-show-remark-5)]
    ;;(println "year: " year "\nseason: " season "\nitems: " items "\nitems-for2010: " items-for2010)
    ;;(println view-html)
    (str "jQuery(\"#view\").html(\"" view-html "\");\n"
         "jQuery(\"#view\").visualEffect(\"slide_down\");")))

