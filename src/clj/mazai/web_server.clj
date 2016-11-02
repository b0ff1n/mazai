(ns mazai.web-server
  (:require [com.stuartsierra.component :as component]
            [taoensso.timbre            :as log]
            [immutant.web               :as s]
            [compojure.core             :refer [defroutes context GET]]
            [compojure.route            :refer [resources not-found]]
            [ring.util.response         :refer [response redirect]]
            [ring.middleware.format     :refer [wrap-restful-format]]
            [clj-jwt.core               :as jwt]))

;; =============================================================================
;; utilities

(defn extract-jwt [req]
  (if-let [auth (get-in req [:headers "authorization"])]
    (last (re-find #"(?i)^(?:Bearer|JWT)\s+(.*)$" auth))
    (get-in req [:query-params "token"])))

;; =============================================================================
;; middlewares

(defn wrap-state
  "Adds components to ring request.
   Use namespaced keywords (::something) so it will not clash with
   ring request keys."
  [handler & {:as state}]
  (fn [req]
    (handler (merge req state))))

(defn wrap-jwt
  "Parses, validates, and attaches the JWT if it exists in the request."
  [handler & {:keys [secret]}]
  (fn [req]
    (if-let [jwt-str (extract-jwt req)]
      (let [jwt (jwt/str->jwt jwt-str)]
        (if (jwt/verify jwt :HS256 secret)
          (handler (assoc req :jwt (:claims jwt)))
          (throw (IllegalStateException. "failed to validate JWT signature"))))
      (handler req))))

;; =============================================================================
;; handlers

(defn dummy []
  (fn [_]
    (response "blah")))

;; =============================================================================
;; routes

(defroutes routes
  (context "/api/:version" [version]
    (GET "/dummy" [] (dummy)))
  (GET "/status" [] (response "ok"))
  (GET "/" [] (redirect "/index-dev.html"))
  (resources "/")
  (not-found "say what?"))

;; =============================================================================
;; component

(defrecord WebServer [listening-port jwt-secret]
  component/Lifecycle
  (start [component]
    (log/info ";; starting WebServer")
    (assoc component
           :server (s/run (-> routes
                              (wrap-state)
                              (wrap-jwt :secret jwt-secret)
                              wrap-restful-format)
                     :host "0.0.0.0"
                     :port listening-port)))
  (stop [{:keys [server] :as component}]
    (log/info ";; stopping WebServer")
    (when server
      (try
        (s/stop server)
        (catch Exception ex
          (log/error ex "failed to stop WebServer"))))
    (dissoc component :server)))

;; =============================================================================
;; constructor

(defn new-web-server [config]
  (component/using
   (map->WebServer (select-keys config [:listening-port :jwt-secret]))
   []))
