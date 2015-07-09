(ns zszj.controllers.equipments_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.equipments :as equipments]
            [selmer.parser :as parser]))

(defn index
  []
  (let [years (sort-by :year > (map (fn [year]
                                      (assoc year :year (Integer/parseInt (:year year))))
                                    (equipments/get-years)))
        months (sort-by :month > (map (fn [month]
                                        (assoc month :month (Integer/parseInt (:month month))))
                                      (equipments/get-months)))
        current-month (common/get-current-month)
        months (nth (split-at current-month months) 1)
        latest-year-month (equipments/get-latest-year-month)
        year (:year latest-year-month)
        month (:month latest-year-month)
        items (common/assoc-index-oddeven (equipments/find-all-by-year-and-month year month))]
    ;;(println "months: " months)
    (layout/render "equipments/index.html"
                   (common/common-manipulate {:year   year
                                              :month  month
                                              :years  years
                                              :months months
                                              :items  items} "jgxx"))))

;;(split-at 3 '(1 2 3 4 5))

(defn generate-items-html
  [items]
  (reduce (fn [html item]
            (str html "<tr onMouseOver=\\\"this.bgColor='#cbeceb';\\\" onmouseout=\\\"this.bgColor='#FFFFFF';\\\" bgcolor=\\\"#ffffff\\\"; bordercolor=\\\"#CCCCCC\\\" class=\\\"" (:odd-even item) "\\\"><td width=\\\"40\\\" class=\\\"info\\\">" (:item-index item) "</td><td width=\\\"180\\\" class=\\\"subject\\\">" (:name item) "</td><td width=\\\"260\\\" class=\\\"info\\\">" (:spec_model item) "</td><td width=\\\"140\\\"  class=\\\"info\\\">" (:period item) "</td><td width=\\\"140\\\"  class=\\\"info\\\">" (:price item) "</td><td width=\\\"180\\\"  class=\\\"info\\\">" (:remark item) "</td></tr>")) "" items))

(defn generate-view
  [items year month]
  (let [items-html (generate-items-html items)]
    (str "<div class=\\\"price_title\\\">" year "年" month "月舟山市机械设备市场租赁参考价</div><table width=\\\"710\\\" height=\\\"25\\\" class=\\\"tableview\\\"><tr><th>序 号</th><th>设备名称</th><th>型号规格</th><th>租 期</th><th>价 格</th><th>备 注</th></tr>" items-html "</table><div class=\\\"price_remark\\\">注：机械租赁价格未包括机上人工及燃料动力费用（挖掘机除外），且不包括机械进退场费。</div>")))

(defn search
  [& ajaxargs]
  ;;(println ajaxargs)
  (let [param (nth ajaxargs 0)
        year (-> param :search :year)
        month (-> param :search :month)
        items (common/assoc-index-oddeven (equipments/find-all-by-year-and-month year month))
        view-html (generate-view items year month)]
    ;;(println "year: " year "\nmonth: " month)
    (str "jQuery(\"#view\").html(\"" view-html "\");\n"
         "jQuery(\"#view\").visualEffect(\"slide_down\");")))
