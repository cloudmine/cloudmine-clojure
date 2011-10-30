# Cloudmine Client

This is a simple client for interfacing with cloudmine (cloudmine.me). It uses native Clojure data structures, so you shouldn't have to deal with any JSON directly.

## How to use

The Cloudmine credentials live in a simple map.

     (def cm-creds {:cm-app-id <your app id> :cm-api-key <your api key>})

Pass these to the other functions to do things with cloudmine.

     (cloudmine/put cm-creds {:key {:data 3 :val 6}})
     (cloudmine/get cm-creds :key)
     (cloudmine/query cm-creds "[val=6]")

If you want user-level credentials, make another map you're using:

     (def user-cm-creds {:email <user email> :password <user password>})

     (cloudmine/put cm-creds user-cm-creds {:key {:data 3 :val 6}})
     (cloudmine/get cm-creds user-cm-creds :key)
     (cloudmine/query cm-creds user-cm-creds "[val=6]")
