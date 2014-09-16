(ns zszj.controllers.common
  (:require [zszj.views.layout :as layout])
  (:import (java.util Calendar GregorianCalendar)
           (java.util Date)
           (java.sql Timestamp)))

(def weekday-map
  {:1 "日"
   :2 "一"
   :3 "二"
   :4 "三"
   :5 "四"
   :6 "五"
   :7 "六"})

(defn- get-current-weekday
  []
  (let [gc (GregorianCalendar.)
        day-of-week (.get gc Calendar/DAY_OF_WEEK)
        is-weekend (or (= day-of-week Calendar/SATURDAY) (= day-of-week Calendar/SUNDAY))]
    day-of-week))

(defn- get-current-datetime
  []
  (let [time (clojure.string/split
              (nth (clojure.string/split
                    (str (Timestamp. (.getTime (Date.)))) #" ") 0) #"-")
        year (nth time 0)
        month (nth time 1)
        day (nth time 2)]
    (str year "年" month "月" day "日")))

(defn- add-navibar
  [var-map current-root-key]
  (merge var-map
         {:menus layout/menus
          :current-root-key current-root-key}))

(defn- add-datetime
  [var-map]
  (merge var-map
         {:week-day (get weekday-map (keyword (str (get-current-weekday))))
          :datetime (get-current-datetime)}))

(defn common-manipulate
  [var-map current-root-key]
  (-> var-map
      (add-navibar current-root-key)
      add-datetime))
