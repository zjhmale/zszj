(ns zszj.views.helpers)

(defn article-path [a]
  (str "/articles/" (:id a)))
