(ns zszj.controllers.materials_controller
  (:require [cheshire.core :as ches]
            [selmer.parser :as parser]
            [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.db.materials :as materials]
            [zszj.controllers.common :as common]))

(def ^:dynamic *PER-PAGE* 20)

(def PER-PAGE 20)

(defn index
  [& args]
  (println args)
  (let [current-page (:page (nth args 0))
        current-page (bigdec (if current-page current-page "1"))
        material-names (materials/get-material-field :name)
        material-specs (materials/get-material-field :spec)
        latest-material-date (helper/date-format-without-brackets
                              (:publish_at (materials/get-latest-material)))
        num-materials (materials/get-materials-count)
        materials (materials/get-materials 20)
        materials-view (map (fn [material]
                         (let [mat-index (inc (.indexOf materials material))
                               odd-even (if (odd? mat-index)
                                          "odd"
                                          "even")]
                           (assoc (assoc material :odd-even odd-even) :mat-index mat-index)))
                       materials)]
    ;;(println "names: " material-names "\nspecs: " material-specs)
    (println "current-page: " current-page)
    (layout/render "materials/index.html"
                   (common/common-manipulate (merge {:material-names material-names
                                                     :material-specs material-specs
                                                     :latest-material-date latest-material-date
                                                     :materials materials-view
                                                     ;;for paginator
                                                     :current-page current-page
                                                     :current-page-dec (dec current-page)
                                                     :current-page-inc (inc current-page)
                                                     :num-articles num-materials}
                                                    (common/paginator num-materials PER-PAGE current-page "/materials")) "jgxx"))))

(defn- mini_view
  [publish_at]
  (let [materials (map (fn [material]
                         (assoc
                             (assoc material :truncate_name (helper/truncate (:name material) 6))
                           :truncate_spec_brand (helper/truncate (str (:spec material) "-" (:brand material)) 10)))
                       (materials/get-materials-by-publish publish_at 11))
        material-table (reduce (fn [html material]
                                 (str html "<tr><td title=\\\"" (:name material) "\\\">" (:truncate_name material) "</td><td title=\\\"" (str (:spec material) "-" (:brand material)) "\\\">" (:truncate_spec_brand material) "</td><td>" (:price material) "</td></tr>")) "" materials)]
    ;;(println material-table)
    (str "\"<table width=\\\"200\\\" class=\\\"cx\\\" ><tr class=\\\"t2\\\"><th>名 称</th><th>规 格</th><th>价 格</th></tr>" material-table "<tr><td class=\\\"more\\\"></td><td class=\\\"more\\\"></td><td class=\\\"more\\\"><a href=\\\"/materials\\\" class=\\\"black t2\\\">...更多</a></td></tr></table>\"")))

(defn mini_search
  [& ajaxargs]
  (let [publish_at (-> (nth ajaxargs 0) :selected :publish_at)]
    ;;(println publish_at)
    (str "jQuery(\"#mini_material_view\").html(" (mini_view publish_at) ");\n"
         "jQuery(\"#mini_material_view\").visualEffect(\"slide_down\");")))

(defn- main-view
  [publish_at name spec]
  (let [materials (materials/get-materials-for-view publish_at name spec)
        material-table (reduce (fn [html material]
                                 (let [mat-index (inc (.indexOf materials material))
                                       isodd? (odd? mat-index)]
                                   (str html "<tr onMouseOver=\\\"this.bgColor='#cbeceb'\\\" onmouseout=\\\"this.bgColor='#FFFFFF'\\\" bgcolor=\\\"#ffffff\\\"bordercolor=\\\"#CCCCCC\\\"class=\\\"" (if isodd? "odd" "even") "\\\"><td width=\\\"40\\\" class=\\\"info\\\">" mat-index  "</td><td width=\\\"90\\\" class=\\\"info\\\">" (:code material) "</td><td width=\\\"160\\\" class=\\\"subject\\\">" (:name material) "</td><td width=\\\"160\\\"  class=\\\"info\\\">" (:spec material) "</td><td width=\\\"90\\\" class=\\\"info\\\">" (:unit material) "</td><td width=\\\"120\\\" class=\\\"info\\\">" (:brand material) "</td><td width=\\\"100\\\"  class=\\\"info\\\">" (:price material) "</td><td width=\\\"120\\\"  class=\\\"info\\\">" (:publish_at material) "</td></tr>"))) "" materials)]
    ;;(println "publish_at: " publish_at "\nname: " name "\nspec: " spec "\nmaterials: " materials)
    ;;(println "table: " material-table)
    (str "<div class=\\\"price_title\\\">舟山市主要材料市场价格</div><table width=\\\"710\\\" height=\\\"25\\\" class=\\\"tableview\\\"><tr><th>序号</th><th>材料代码</th><th>材料名称</th><th>规格型号</th><th>单 位</th><th>品 牌</th><th>价 格</th><th>日期</th></tr>" material-table "</table>")))

(defn search
  [& ajaxargs]
  (let [param (nth ajaxargs 0)
        publish_at (-> param :selected :publish_at)
        name (-> param :selected :name)
        spec (-> param :selected :spec)
        main-view (main-view publish_at name spec)]
    (str "jQuery(\"#view\").html(\"" main-view "\");\n"
         "jQuery(\"#view\").visualEffect(\"slide_down\");")))

;;just show how to use with jquery datepicker
(defn datepicker
  []
  (layout/render "materials/datepicker.html" {}))




