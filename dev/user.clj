(ns user
  (:require [com.stuartsierra.component   :as component]
            [clojure.repl                 :refer [doc find-doc source]]
            [clojure.pprint               :refer [pprint]]
            [clojure.tools.namespace.repl :refer [refresh]]
            [mazai.system         :refer [new-system]]))

(def dev-config
  {:web-server {:listening-port 8080}})

(def system nil)

(defn init []
  (alter-var-root #'system (constantly (new-system dev-config))))

(defn start []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system (fn [s] (when s (component/stop s)))))

(defn go []
  (init)
  (start)
  :running)

(defn reset []
  (stop)
  (refresh :after 'user/go))
