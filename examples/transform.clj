(ns examples.transform
  (:require [will08rien.prg :refer [run-pipe]]
            [will08rien.prg.stdio :refer [json-lines-in json-lines-out]]))

(defn transform [in]
  {:quux (get-in in [:baz :qux])
   :waldo {:foo (get-in in [:foo])}
   :bar (get-in in [:bar])})

;; read input in the form of a set of linefeed separated json
;; documents, perform a transform on clojure data structures and
;; then emit the payload in json again.
(run-pipe
 {:input json-lines-in
  :filters [transform]
  :outputs [json-lines-out]})
