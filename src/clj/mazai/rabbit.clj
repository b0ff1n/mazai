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

(defprotocol MessageBroker
  (consume [this queue exchange route callback] "Create temporary queue, bind it and consume"))

;; =============================================================================
;; component

(defrecord Rabbit [uri]

  component/Lifecycle

  (start [this]
         (let [conn (rmq/connect {:uri uri})
               ch (lch/open conn)]
           (log/info ";; Starting AMQP" this)
           (assoc this :conn conn :ch ch)))

  (stop [this]
        (log/info ";; Stopping AMQP" this)
        (when-let [ch (:ch this)]
          (lch/close ch))
        (when-let [conn (:conn this)]
          (rmq/close conn))
        (dissoc this :conn))

  MessageBroker

  (consume [{ch :ch} queue exchange route callback]
           (let [delivery-fn (fn [ch metadata ^bytes payload]
                               (callback (parse-string (decode-bytes payload) true)))
                 consumer (lc/create-default ch {:handle-delivery-fn delivery-fn})
                 q (lq/declare ch queue)]
             (lq/bind ch queue exchange {:routing-key route})
             (lb/consume ch queue consumer))))


;; =============================================================================
;; constructor

(defn new-rabbit [config]
  (map->Rabbit (select-keys config [:uri])))
