(ns mazai.core
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.reader.edn   :as edn]
            [mazai.system       :refer [initialize-logger! new-system]])
  (:gen-class))

(def system nil)

(defn -main [config-file]
  (let [config (edn/read-string (slurp config-file))]
    (initialize-logger! (:logger config))
    (alter-var-root #'system (fn [_] (component/start (new-system config))))
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. #(component/stop system)))))
