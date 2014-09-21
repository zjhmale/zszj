(ns zszj.controllers.zhongjies_controller
  (:require [zszj.views.layout :as layout]
            [zszj.views.helper :as helper]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.db.zhongjies :as zhongjies]))

(def title-map
  {"zhongjies" "造价咨询企业"
   "zhaobiaos" "招标代理机构"
   "zjshis" "造价工程师"
   "gaisuans" "造价员"})

(def ^:dynamic *PER-PAGE* 20)

(def PER-PAGE 20)

(defn index
  [type & params]
  (let [subtitle (get title-map type)
        current-page (-> (nth params 0) :page)
        current-page (bigdec (if current-page current-page "1"))
        zhongjies (common/assoc-index-oddeven (zhongjies/get-zhongjies (* (dec current-page) PER-PAGE) PER-PAGE))
        num-zhongjies (zhongjies/get-zhongjies-count)
        latest-updatetime (clojure.string/split (nth (clojure.string/split (str (zhongjies/get-latest-updatetime)) #" ") 0) #"-")
        latest-updatetime (str (nth latest-updatetime 0) "年" (nth latest-updatetime 1) "月" (nth latest-updatetime 2) "日")]
    (println "type: " type "\nsubtitle: " subtitle "\ncurrentpage: " current-page "\nzhongjies: " zhongjies "\nlatest-updatetime: " (str latest-updatetime))
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
                           (common/paginator num-zhongjies PER-PAGE current-page "/zhongjies")) "zzzg"))))

(defn search
  [& ajaxargs]
  "cleantha")
