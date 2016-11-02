(ns mazai.rabbit
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre :as log]
            [cheshire.core :refer [parse-string generate-string]]
            [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]
            [mazai.compress :refer [decode-bytes]]))


;; =============================================================================
;; component

(defrecord Rabbit [uri]

  component/Lifecycle

  (start [this]
         (let [conn (rmq/connect {:uri uri})]
           (log/info ";; Starting AMQP" this)
           (assoc this :conn conn)))

  (stop [this]
        (log/info ";; Stopping AMQP" this)
        (when-let [conn (:conn this)]
          (rmq/close conn))
        (dissoc this :conn)))


;; =============================================================================
;; constructor

(defn new-rabbit [config]
  (map->Rabbit (select-keys config [:uri])))
