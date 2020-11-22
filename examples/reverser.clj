(ns examples.reverser
  (:require [will08rien.prg :refer [run-pipe]]
            [clojure.string :as s]
            [will08rien.prg.stdio :refer [lines-in lines-out]]))

;; read input in the form of a set of linefeed separated documents,
;; reverse the strings and emit the payloads to stdout.
(run-pipe
 {:input lines-in
  :filters [s/reverse]
  :outputs [lines-out]})
