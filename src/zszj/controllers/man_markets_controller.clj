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
                                              :is-show-items (not (empty? items))} "jgxx"))))

;;(clojure.set/difference #{{:three 3} {:two 2} {:one 1}} #{{:one 1}})

(defn view
  []
  (parser/render (slurp "resources/views/man_markets/view_refresh.html") {:test "cleantha"}))

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
        items-for2010 (assoc-index-oddeven items-for2010)]
    ;;(println "year: " year "\nseason: " season "\nitems: " items "\nitems-for2010: " items-for2010)
    (println (view))
    (str "jQuery(\"#view\").visualEffect(\"slide_down\");")))

