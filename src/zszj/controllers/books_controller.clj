(ns zszj.controllers.books_controller
  (:require [zszj.views.layout :as layout]
            [zszj.controllers.common :as common]
            [zszj.db.core :as db]
            [zszj.db.books :as books]
            [zszj.views.helper :as helper]))

(def ^:dynamic *PER-PAGE* 20)

(def PER-PAGE 20)

(defn index
  [& params]
  ;;(println params)
  (let [book_type (-> (nth params 0) :book_type)
        books (common/assoc-index-oddeven (books/find-book-by-type book_type PER-PAGE))
        num-books (count books)
        num-books (if (> num-books PER-PAGE) PER-PAGE num-books)]
    ;;(println "booktype: " book_type "\nbooks: " books "\nfirstbook: " (first books) "\nnum-books: " num-books)
    (layout/render "books/index.html"
                   (common/common-manipulate
                    (merge {:books books
                            :first-book (first books)
                            ;;for paginator
                            :num-articles num-books}
                           (common/paginator num-books PER-PAGE 1 (str "/books"))) "wssx"))))

(defn generate-items-html
  [items]
  (reduce (fn [html item]
            (str html "<tr onMouseOver=\\\"this.bgColor='#cbeceb';\\\" onmouseout=\\\"this.bgColor='#FFFFFF';\\\" bgcolor=\\\"#ffffff\\\"; bordercolor=\\\"#CCCCCC\\\" class=\\\"" (:odd-even item) "\\\"><td width=\\\"40\\\" class=\\\"info\\\">" (:item-index item) "</td><td width=\\\"300\\\" class=\\\"subject\\\">" (:name item) "</td><td width=\\\"90\\\" class=\\\"info\\\">" (:price item)"</td><td width=\\\"140\\\"  class=\\\"info\\\">" (:press item) "</td><td width=\\\"140\\\"  class=\\\"info\\\">" (:book_type item) "</td></tr>")) "" items))

(defn paginator-html
  [from to num-items]
  (str "<div class=\\\"paginate\\\">显示<b>" from " - " to " , 共 " num-items " 条记录</b></div>"))

(defn generate-html
  [items]
  (let [items-html (generate-items-html items)]
    (str "<table width=\\\"710\\\" height=\\\"25\\\" class=\\\"tableview\\\"><tr><th>序 号</th><th>书 名</th><th>价 格</th><th>出版社</th><th>类 别</th></tr>" items-html "</table>")))

(defn search
  [& ajaxargs]
  ;;(println ajaxargs)
  (let [book_type (-> (nth ajaxargs 0) :book_type)
        search_str (-> (nth ajaxargs 0) :search_str)
        books (common/assoc-index-oddeven (books/search-book search_str PER-PAGE))
        books-html (generate-html books)
        num-books (count books)
        num-books (if (> num-books PER-PAGE) PER-PAGE num-books)
        current-page 1
        from (inc (* (dec current-page) PER-PAGE))
        to (min (* current-page PER-PAGE) num-books)
        paginator-html (paginator-html from to num-books)]
    ;;(println "books: " books)
    (str "jQuery(\"#view\").html(\"" books-html paginator-html "\");\n"
         "jQuery(\"#view\").visualEffect(\"slide_down\");")))
