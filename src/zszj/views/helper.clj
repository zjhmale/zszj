(ns zszj.views.helper)

(defn article-path [a]
  (str "/articles/" (:id a)))
