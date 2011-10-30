(ns cloudmine
  (:refer-clojure :exclude [get])
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:require [clojure.core :as core])
  (:require [clj-http.client :only (post)  :as client]))

(defn response-body
  [r]
  (-> r :body read-json))

(defn- keys-string
  [keys]
  (if (empty? keys) ""
      (str (reduce (fn [acc x]
                     (str acc "," x))
                   (first keys) (next keys)))))

(defn put
  "Adds data to cloudmine. Pass in credentials as a map, and
   uses data-key as the key to put the data. data-key should
   be a clojure keyword, data should be a native clojure data
   structure."
  [{cm-app-id :cm-app-id cm-api-key :cm-api-key} data]
  (client/put (str "https://api.cloudmine.me/v1/app/"
                     cm-app-id
                     "/text")
                {:body (json-str data)
                 :headers {"X-Cloudmine-ApiKey" cm-api-key}
                 :content-type :application/json}))

(defn get
  "Queries cloudmine by keys"
  [{cm-app-id :cm-app-id cm-api-key :cm-api-key} & keys]
  (client/get (str
               "https://api.cloudmine.me/v1/app/"
               cm-app-id
               "/text?keys="
               (keys-string keys))
              {:headers {"X-Cloudmine-ApiKey" cm-api-key}
               :content-type :application/json
               :accepts :application/json}))

(defn query
  "Queries cloudmine using search query language"
  [{cm-app-id :cm-app-id cm-api-key :cm-api-key} query]
  (client/get (str
               "https://api.cloudmine.me/v1/app/"
               cm-app-id
               "/search?q="
               query)
              {:headers {"X-Cloudmine-ApiKey" cm-api-key}
               :content-type :application/json
               :accepts :application/json}))