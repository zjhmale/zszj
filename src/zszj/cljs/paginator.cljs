(ns zszj.cljs.paginate
  (:use [jayq.core :only [$]])
  (:require [jayq.core :as jq]))

(def $clickhere ($ :#clickhere))
 
(jq/bind $clickhere :click (fn [evt] (js/alert "Clicked!!")))

(def $interface ($ :#interface))

(-> $interface
  (jq/css {:background "blue"})
  (jq/html "Loading!"))
