(ns will08rien.prg.stdio
  (:require [clojure.data.json :as json]))

(defn lines-in [cb]
  (doseq [line (line-seq (java.io.BufferedReader. *in*))]
    (cb line)))

(defn lines-out [msg]
  (println msg))

(defn json-lines-in [cb]
  (doseq [line (line-seq (java.io.BufferedReader. *in*))]
    (-> line
        (json/read-str :key-fn keyword)
        (cb))))

(defn json-lines-out [msg]
  (println (json/write-str msg))
  msg)
