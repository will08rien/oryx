(ns will08rien.prg.http
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response]]))

(defn http-filter [params]
  (fn [input]
    (let [resp (client/post (:url params)
                            {:body (json/write-str (:data input))
                             :as :json})]
      {:data (json/read-json (get-in resp [:body :data]))})))

(defn handler [cb]
  (fn [request]
    (try 
      (response (cb {:data (:body request)}))
      (catch java.lang.AssertionError e
        (println (ex-message e))
        {:status 500
         :headers {}
         :body {:error true}}))))
         
(defn http [cb]
  (run-jetty (-> (handler cb)
                 (wrap-json-body  {:keywords? true})
                 (wrap-json-response))
             {:port  3000 :join? false}))
