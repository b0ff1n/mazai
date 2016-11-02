(defproject mazai "0.2.0"
  :description "TODO: FIXME"
  :url "TODO: FIXME"
  :min-lein-version "2.5.2"

  :dependencies [[org.clojure/clojure        "1.7.0"]
                 [org.clojure/core.async     "0.2.374"]
                 [com.stuartsierra/component "0.3.1"]
                 [com.taoensso/timbre        "4.1.4"]
                 [org.immutant/web           "2.1.1"]
                 [ring/ring-core             "1.4.0"]
                 [ring-middleware-format     "0.7.0"]
                 [compojure                  "1.4.0"]
                 [clj-jwt                    "0.1.1"]
                 [clj-utils                  "0.1.2"]
                 [org.clojure/test.check     "0.9.0"]]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :main ^:skip-aot mazai.core
  :global-vars {*print-length* 100}
  :target-path "target/%s"
  :uberjar-name "mazai-standalone.jar"
  :repl-options {:init-ns user}
  :profiles {:uberjar {:aot :all
                       :omit-source true
}
             :dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]
                                  [org.clojure/tools.nrepl     "0.2.12"]]
                   :source-paths ["dev"]
                   :cljsbuild {:builds [{:id "dev"
                                         :source-paths ["src/cljs"]
                                         :test-paths   ["test/cljs"]
                                         :compiler {:main         mazai.core
                                                    :asset-path   "js/out"
                                                    :output-to    "resources/public/js/app.js"
                                                    :output-dir   "resources/public/js/out"
                                                    :source-map    true
                                                    :optimizations :none
                                                    :pretty-print  true}}]}}})
