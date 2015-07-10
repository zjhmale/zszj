(ns zszj.test.db
  (:require [clojure.test :refer :all]
            [zszj.db.full-texts :as fulltexts]))

(deftest test-get-cats
  (testing "get all categories from full_texts table"
    (prn (fulltexts/get-cats))))

(deftest test-search-by-cat
  (testing "search from full_texts table by search_str and search_cat"
    (prn (fulltexts/search-by-cat "2015" "招标代理机构"))))
