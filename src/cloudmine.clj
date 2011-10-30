(ns cloudmine
  (:refer-clojure :exclude [get])
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:require [clojure.core :as core])
  (:require [clj-http.client :only (post)  :as client]))

(defn put-json
  "Adds data to cloudmine. Pass in credentials as a map, and
   uses data-key as the key to put the data. data-key should
   be a clojure keyword, data should be a native clojure data
   structure."
  [{cm-app-id :cm-app-id cm-api-key :cm-api-key} data-key data]
  (read-json (:body (client/post (str "https://api.cloudmine.me/v1/app/"
                                      cm-app-id
                                      "/"
                                      (name data-key)
                                      "/text")
                                 {:body (json-str data)
                                  :headers {"X-Cloudmine-ApiKey" cm-api-key}
                                  :content-type :application/json}))))

(defn get-json
  "Grabs data from cloudmine. Key should be a keyword."
  [{cm-app-id :cm-app-id cm-api-key :cm-api-key} data-key]
  (read-json (:body (client/get (str
                                 "https://api.cloudmine.me/v1/app/"
                                 cm-app-id
                                 "/"
                                 (name data-key)
                                 "/text")
                                {:headers {"X-Cloudmine-ApiKey" cm-api-key}
                                 :content-type :application/json
                                 :accepts :application/json}))))

(defn query-json
  "Queries cloudmine"
  [{cm-app-id :cm-app-id cm-api-key :cm-api-key} data-key query]
  (read-json (:body (client/get (str
                                 "https://api.cloudmine.me/v1/app/"
                                 cm-app-id
                                 "/"
                                 (name data-key)
                                 "/text"
                                 "/search?q="
                                 query)
                                {:headers {"X-Cloudmine-ApiKey" cm-api-key}
                                 :content-type :application/json
                                 :accepts :application/json}))))