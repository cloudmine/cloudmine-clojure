(ns cloudmine
  (:refer-clojure :exclude [get])
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:require [clojure.core :as core]
            [clj-http.client :only (post)  :as client]
            [clojure.data.codec.base64 :as b64]))

(def cloudmine-api-url "https://api.cloudmine.me/v1/app/")

(defn- response-body
  [r]
  (-> r :body read-json))

(defn- keys-string
  [keys]
  (if (empty? keys) ""
      (str (reduce (fn [acc x]
                     (str acc "," x))
                   (first keys) (next keys)))))

(defn- base-64-str
  [s]
  (new String
       (b64/encode
        (.getBytes s))))

(defn- build-cm-request
  ([api-auth action]
      {:params {:headers {"X-Cloudmine-ApiKey" (:cm-api-key api-auth)}
                :content-type :application/json}
       :url (str cloudmine-api-url (:cm-app-id api-auth) "/" action)})
  ([api-auth user-auth action]
      {:params {:headers {"X-Cloudmine-ApiKey" (:cm-api-key api-auth)
                          "Authorization" (base-64-str
                                           (str (:email user-auth)
                                                ":"
                                                (:password user-auth)))}
                :content-type :application/json}
       :url (str cloudmine-api-url (:cm-app-id api-auth) "/user/" action)}))

(defn- put-internal
  [url params data]
  (response-body
   (client/put url
               (conj params
                     {:body (json-str data)}))))

(defn put
  "Adds data to cloudmine. Pass in credentials as a map, and
   uses data-key as the key to put the data. data-key should
   be a clojure keyword, data should be a native clojure data
   structure."
  ([auth data]
      (let [{:keys [url params]} (build-cm-request auth "text")]
        (put-internal url params data)))
  ([auth user-auth data]
      (let [{:keys [url params]} (build-cm-request auth user-auth)]
        (put-internal url params data))))

(defn- get-internal
  [url params keys]
  (response-body (client/get (str url "?keys="
                                  (keys-string (map name keys)))
                             params)))

(defn get
  "Queries cloudmine by keys"
  ([auth keys]
      (let [{:keys [url params]} (build-cm-request auth "text")]
        (get-internal url params keys)))
  ([auth user-auth keys]
      (let [{:keys [url params]} (build-cm-request auth user-auth "text")]
        (get-internal url params keys))))

(defn- query-internal
  [url params query]
    (response-body (client/get (str url "?q=" query)
                               params)))
(defn query
  "Queries cloudmine using search query language"
  ([auth query]
      (let [{:keys [url params]} (build-cm-request auth "search")]
        (query-internal url params query)))
  ([auth user-auth query]
      (let [{:keys [url params]} (build-cm-request auth user-auth "search")]
        (query-internal url params query))))
