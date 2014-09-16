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

(defn- format-date [format date]
  (.format (java.text.SimpleDateFormat. format) date))

(defn date-format
  [date]
  (str "[" (format-date "yyyy-MM-dd" date) "] "))

(defn date-format-without-brackets
  [date]
  (format-date "yyyy-MM-dd" date))
