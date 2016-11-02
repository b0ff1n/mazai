(ns mazai.system
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre            :as log]
            [logstash.logstash          :refer [logstash-appender]]
            [mazai.web-server   :refer [new-web-server]]))

(defn initialize-logger! [{:keys [min-level uri] :or {min-level :info}}]
  (log/merge-config!
   {:appenders {:logstash (logstash-appender {:uri uri :min-level min-level})}}))

(defn new-system [config]
  (component/system-map
   :web-server (new-web-server (:web-server config))))
