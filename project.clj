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
                 [org.clojure/tools.namespace "0.2.5"]
                 [lib-noir "0.8.4"]
                 [org.clojure/clojurescript "0.0-2322"]
                 [jayq "2.5.2"]]
  :plugins [[lein-cljsbuild "1.0.3"]
            [lein-ring "0.8.11"]]
  :cljsbuild {
    :builds [{
        ; The path to the top-level ClojureScript source directory:
        :source-paths ["src/zszj/cljs"]
        ; The standard ClojureScript compiler options:
        ; (See the ClojureScript compiler documentation for details.)
        :compiler {
          :output-to "resources/public/javascripts/paginator.js"  ; default: target/cljsbuild-main.js
          :optimizations :whitespace
          :pretty-print true}}]}
  :ring {:handler zszj.handler/app
         :stacktraces? true
         :auto-reload true
         :auto-refresh? true
         :init zszj.handler/init
         :destroy zszj.handler/destroy}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
