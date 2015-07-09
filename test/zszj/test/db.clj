(ns zszj.test.db
  (:require [clojure.test :refer :all]
            [zszj.db.full-texts :as fulltexts]))

(deftest test-get-cats
  (testing "get all categories from full_texts table"
    (prn (fulltexts/get-cats))))
