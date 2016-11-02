(ns mazai.system
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre            :as log]
            [mazai.rabbit       :refer [new-rabbit]]
            [mazai.web-server   :refer [new-web-server]]))

(defn new-system [config]
  (component/system-map
   :rabbit (new-rabbit (:rabbit config))
   :web-server (new-web-server (:web-server config))))
