(ns will08rien.prg)

(defn run-pipe [pipe-config]
  ((:input pipe-config)
   (fn [input]
     (let* [f (apply comp (:filters pipe-config))
            fd (f input)]
       ((nth (:outputs pipe-config) 0) fd)))))

