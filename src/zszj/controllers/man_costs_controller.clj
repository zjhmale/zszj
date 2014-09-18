(ns zszj.controllers.man_costs_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.man_costs :as man_costs]
            [selmer.parser :as parser]))

(defn index
  []
  (let [all-year-seasons (common/sort-year-and-season (man_costs/get-year-season))
        latest-year-season (man_costs/get-latest-year-season)
        year (:year latest-year-season)
        season (:season latest-year-season)
        items (common/assoc-index-oddeven (man_costs/find-all-by-year-and-season year season))]
    ;;(println "all-year-seasons: " all-year-seasons "\nyear: " year "\nseason: " season "\nitems: " items)
    (layout/render "man_costs/index.html"
                   (common/common-manipulate {:all-year-seasons all-year-seasons
                                              :year year
                                              :season season
                                              :items items} "jgxx"))))

(defn- generate-items-html
  [items]
  (reduce (fn [html item]
            (str html "<tr onMouseOver=\\\"this.bgColor='#cbeceb';\\\" onmouseout=\\\"this.bgColor='#FFFFFF';\\\" bgcolor=\\\"#ffffff\\\"; bordercolor=\\\"#CCCCCC\\\" class=\\\"" (:odd-even item) "\\\"><td width=\\\"40\\\" class=\\\"info\\\">" (:item-index item) "</td><td width=\\\"220\\\" class=\\\"subject\\\">" (:man_type item) "</td><td width=\\\"220\\\" class=\\\"info\\\">" (:per_salary item) "</td></tr>")) "" items))

(defn- generate-view
  [items year season]
  (let [items-html (generate-items-html items)]
    (str "<div class=\\\"price_title\\\">" year "年第" season "季度舟山市建筑工种人工成本信息价</div><table width=\\\"710\\\" height=\\\"25\\\" class=\\\"tableview\\\"><tr><th>序 号</th><th>工 种</th><th>日工资</th></tr>" items-html "</table>")))

(defn search
  [& ajaxargs]
  (let [params (clojure.string/split (-> (nth ajaxargs 0) :selected :season) #"-")
        year (nth params 0)
        season (nth params 1)
        items (common/assoc-index-oddeven (man_costs/find-all-by-year-and-season year season))
        view-html (generate-view items year season)]
    ;;(println "year: " year "\nseason: " season)
    (str "jQuery(\"#view\").html(\"" view-html "\");\n"
         "jQuery(\"#view\").visualEffect(\"slide_down\");")))
