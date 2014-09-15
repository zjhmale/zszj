(defproject zszj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [korma "0.3.2"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [ring-server "0.3.1"]
                 [compojure "1.1.8"]
                 [selmer "0.7.1"]
                 [environ "0.5.0"]
                 [enlive "1.1.5"]
                 [hiccup "1.0.5"]
                 [org.clojure/tools.namespace "0.2.5"]
                 [lib-noir "0.8.4"]]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler zszj.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
