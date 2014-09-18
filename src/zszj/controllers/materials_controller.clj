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
  ;;(println args)
  (let [param (nth args 0)
        publish_at (-> param :selected :publish_at)
        name (-> param :selected :name)
        spec (-> param :selected :spec)
        current-page (-> param :page)
        current-page (bigdec (if current-page current-page "1"))
        material-names (materials/get-material-field :name)
        material-specs (materials/get-material-field :spec)
        latest-material-date (helper/date-format-without-brackets
                              (:publish_at (materials/get-latest-material)))
        is-all-empty (and (empty? publish_at)
                          (empty? name)
                          (empty? spec))
        query-offset (* (dec current-page) PER-PAGE)
        num-materials (if (not is-all-empty)
                        (materials/get-materials-count-for-view publish_at name spec)
                        (materials/get-materials-count))
        materials (if (not is-all-empty)
                    (materials/get-materials-for-view-with-offset query-offset publish_at name spec)
                    (materials/get-materials-with-offset query-offset 20))
        materials-view (map (fn [material]
                              (let [mat-index (+ (int query-offset)
                                                 (inc (.indexOf materials material)))
                                    odd-even (if (odd? mat-index)
                                               "odd"
                                               "even")]
                                (assoc (assoc material :odd-even odd-even) :mat-index mat-index)))
                            materials)]
    ;;(println "names: " material-names "\nspecs: " material-specs)
    ;;(println "current-page: " current-page "\nall-empty?: " (and (empty? publish_at) (empty? name) (empty? spec)) "\nquery-offset: " query-offset)
    ;;(println "is-paginate-for-allnotempty: " @common/is-paginate-for-notallempty)
    (do
      (if (and (empty? (-> param :page))
               is-all-empty)
        ;;(println "page and other three param are all empty")
        (if @common/is-paginate-for-notallempty
          (swap! common/is-paginate-for-notallempty not)))
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
                                                      (if (or (not is-all-empty) @common/is-paginate-for-notallempty)
                                                        (let [base-uri (str "materials?_=1410958445083&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&selected[name]=" name "&selected[publish_at]=" publish_at "&selected[spec]=" spec "")]
                                                          (common/paginator num-materials PER-PAGE current-page base-uri "notallempty"))
                                                        (common/paginator num-materials PER-PAGE current-page "/materials"))) "jgxx")))))

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

(def NUM-HEAD-LINKS 3)
(def NUM-TAIL-LINKS 3)
(def NUM-PREV-LINKS 4)
(def NUM-POST-LINKS 4)

(defn- generate-html
  [collections base-uri]
  (reduce (fn [html link]
            (str html "<a href=\\\"" base-uri "&page=" link "\\\">" link "</a><span> </span>")) "" collections))

(defn- paginator
  [current-page publish_at name spec]
  (let [num-materials (materials/get-materials-count-for-view publish_at name spec)
        num-pages (-> (/ num-materials PER-PAGE) int inc)
        links-from (- current-page NUM-PREV-LINKS)
        links-from (if (< links-from 1)
                     1
                     links-from)
        links-to (+ current-page NUM-POST-LINKS)
        links-to (if (> links-to num-pages) num-pages
                     links-to)
        head-links (range 1 (inc (min (- links-from 1)
                                      NUM-HEAD-LINKS)))
        tail-links (range (max (- num-pages NUM-TAIL-LINKS)
                               (+ links-to 1)) num-pages)
        from-current-links (range links-from current-page)
        current-to-links (range (inc current-page) (inc links-to))
        current-page-dec (dec current-page)
        current-page-inc (inc current-page)
        base-uri (str "materials?_=1410958445083&authenticity_token=7NSlgmbOtICU2RXWQZScwwMzqVc/tUZbCDf3TKzbmj0=&selected[name]=" name "&selected[publish_at]=" publish_at "&selected[spec]=" spec "")
        from (inc (* (dec current-page) PER-PAGE))
        to (min (* current-page PER-PAGE) num-materials)
        is-last-page (>= current-page num-pages)
        head-links-html (generate-html head-links base-uri)
        from-current-links-html (generate-html from-current-links base-uri)
        current-to-links-html (generate-html current-to-links base-uri)
        tail-links-html (generate-html tail-links base-uri)]
    (str "<div class=\\\"paginate\\\">显示<b>" from " - " to ", 共 " num-materials " 条记录</b><span> </span><div class=\\\"pagination\\\">" (if (< current-page 2) (str "<span class=\\\"prev_page\\\">« 上一页</span>") (str "<a class=\\\"prev_page\\\" rel=\\\"prev start\\\" href=\\\"" base-uri "&page=" current-page-dec "\\\">« 上一页</a>")) "<span> </span>" head-links-html from-current-links-html "<span class=\\\"current\\\">" current-page "</span>" current-to-links-html tail-links-html (if is-last-page (str "<span class=\\\"disabled next_page\\\">下一页 »</span>") (str "<a class=\\\"next_page\\\" rel=\\\"next\\\" href=\\\"" base-uri "&page=" current-page-inc "\\\">下一页 »</a>")) "</div></div>")))

(defn search
  [& ajaxargs]
  (let [param (nth ajaxargs 0)
        publish_at (-> param :selected :publish_at)
        name (-> param :selected :name)
        spec (-> param :selected :spec)
        current-page (-> param :page)
        current-page (bigdec (if current-page current-page "1"))
        main-view (main-view publish_at name spec)
        paginator-view (paginator current-page publish_at name spec)]
    (str "jQuery(\"#view\").html(\"" main-view paginator-view "\");\n"
         "jQuery(\"#view\").visualEffect(\"slide_down\");")))

;;just show how to use with jquery datepicker
(defn datepicker
  []
  (layout/render "materials/datepicker.html" {}))
