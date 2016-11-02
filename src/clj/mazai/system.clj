(ns mazai.system
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre            :as log]
            [mazai.web-server   :refer [new-web-server]]))

(defn new-system [config]
  (component/system-map
   :web-server (new-web-server (:web-server config))))
