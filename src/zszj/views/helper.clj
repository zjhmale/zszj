(ns zszj.views.helper)

(defn article-path [a]
  (str "/articles/" (:id a)))

(defn truncate_u [s length]
  (if (< (count s) length)
    s
    (str (->> s (take length) (apply str)) "...")))

(defn truncate [s length]
  (if (< (count s) length)
    s
    (->> s (take length) (apply str))))

;;(truncate_u "cleanthaasdasdas" 10)

