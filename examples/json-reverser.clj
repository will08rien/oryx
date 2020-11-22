(ns examples.reverser
  (:require [will08rien.prg :refer [run-pipe]]
            [clojure.string :as s]
            [will08rien.prg.stdio :refer [json-lines-in json-lines-out]]))

;; read input in the form of a set of linefeed separated json
;; documents, reverse the strings associated with the key "a", and
;; emit the payloads to stdout.
(run-pipe
 {:input json-lines-in
  :filters [(fn [x]
              (assoc-in x [:data :a]
                        (s/reverse (:a (:data x)))))]
  :outputs [json-lines-out]})
