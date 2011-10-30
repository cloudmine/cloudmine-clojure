(ns cloudmine
  (:refer-clojure :exclude [get])
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:require [clojure.core :as core])
  (:require [clj-http.client :only (post)  :as client]))

(def cloudmine-api-url "https://api.cloudmine.me/v1/app/")

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
  (response-body (client/put (str cloudmine-api-url
                                  cm-app-id
                                  "/text")
                             {:body (json-str data)
                              :headers {"X-Cloudmine-ApiKey" cm-api-key}
                              :content-type :application/json})))

(defn get
  "Queries cloudmine by keys"
  [{cm-app-id :cm-app-id cm-api-key :cm-api-key} & keys]
  (response-body (client/get (str
                              cloudmine-api-url
                              cm-app-id
                              "/text?keys="
                              (keys-string (map name keys)))
                             {:headers {"X-Cloudmine-ApiKey" cm-api-key}
                              :content-type :application/json
                              :accepts :application/json})))

(defn query
  "Queries cloudmine using search query language"
  [{cm-app-id :cm-app-id cm-api-key :cm-api-key} query]
  (response-body (client/get (str
                              cloudmine-api-url
                              cm-app-id
                              "/search?q="
                              query)
                             {:headers {"X-Cloudmine-ApiKey" cm-api-key}
                              :content-type :application/json
                              :accepts :application/json})))