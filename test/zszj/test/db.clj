(ns zszj.test.db
  (:require [clojure.test :refer :all]
            [zszj.db.full-texts :as fulltexts]
            [zszj.controllers.search-controller :as searchcontroller]))

(deftest test-get-cats
  (testing "get all categories from full_texts table"
    (prn (fulltexts/get-cats))))

(deftest test-search-by-cat
  (testing "search from full_texts table by search_str and search_cat"
    (prn (fulltexts/search-by-cat "2015" "招标代理机构" 0 20))))

(deftest test-highlight-search-text
  (testing "hight the search text in full text"
    (let [search_str "工程造价"
          fulltexts [{:full_text "我委组织召开贯彻新版施工合同（示范文本）工作座谈会 \n 近日，我委与市工商行政管理局联合印发了&ldquo;转发关于做好《建设工程施工合同（示范文本）》（GF-2013-0201）的通知&rdquo;（舟建委[2013]151号），自今年8月1日起，我市将全面贯彻执行2013版施工合同（示范文本）。为进一步掌握2013版施工合同（示范文本）精神，我委于29日组织召开了贯彻2013版施工合同（示范文本）工作座谈会。委政策法规处，建筑业管理处，市造价站、市招标办负责人，各县（区）建设主管部门、金塘与六横管委会分管领导与相关科室负责人及全市工程造价咨询、招标代理机构（含分支机构）企业负责人参加了座谈会。\n\n 市住建委党委委员、市城管执法局胡云龙副局长在会上从施工合同（示范文本）修改的背景、修改的原则和修改的特点三方面强调充分认识2013版施工合同（示范文本）的作用和意义，并就如何贯彻实施2013版施工合同（示范文本）提出三点意见：一是要加强2013版施工合同（示范文本）宣贯工作；二是要加强施工合同履约管理，三是要市场各方主体自觉实施2013版施工合同（示范文本）。\n\n\n\n"}]]
      (prn (searchcontroller/highlight-search-text fulltexts search_str)))))

(deftest test-get-fulltexts-count
  (testing "get fulltexts count"
    (prn (fulltexts/get-fulltexts-count))))
