# Prg

Prg (pronounced porridge) is a flexible data pipeline, which
prioritises data governance.

## Data format

Inputs will provide a map with :data and :metadata keys for filters to process. Outputs will handle data and metadata in context sensitive way.

Data and metadata schema can be independently defined.

## Examples

### Reverser

As an initial motivating example, consider the following code, to run
stdin through a simple filter and emit data to stdout.

```clojure
;; read input in the form of a set of linefeed separated json
;; documents, reverse the strings associated with the key "a", and
;; emit the payloads to stdout.
(run-pipe
 {:input lines-in
  :filters [s/reverse]
  :outputs [lines-out]})
```

Running the pipe on the command line yields the following behaviour:

```
$ echo 'Hello, World!
Murder for a jar of redrum' | clojure -M examples/reverser.clj
!dlroW ,olleH
murder fo raj a rof redruM
```

### Json Reverser

Typically, we can expect to be dealing with structured inputs and
outputs, so there are convenience functions for writing filters that
operate on clojure data structures without needing to add
preprocessing in the pipeline. Take the following example:

```clojure
;; read input in the form of a set of linefeed separated json
;; documents, reverse the strings associated with the key "a", and
;; emit the payloads to stdout.
(run-pipe
 {:input json-lines-in
  :filters [(fn [x]
              (assoc-in x [:data :a]
                        (s/reverse (:a (:data x)))))]
  :outputs [json-lines-out]})
```

This results in the following type of workflow:

```
$ echo '{"data": {"a": "hello"}}
{"data": {"a": "abc"}}' | clojure -M examples/json-reverser.clj
{"data":{"a":"olleh"}}
{"data":{"a":"cba"}}
```

### Transformer

One key ingredient of ETL is, of course, the transform. Being able to
manipulate inputs as first class clojure data structures using the
entire ecosystem available in normal clojure programs (or indeed from
the entire JVM ecosystem) makes development of transforms trivial.

Consider the following input:

```
{
  "foo": 1,
  "bar": 2,
  "baz": {
    "qux": 3
  },
  "quux": "garply"
}
```

Performing transforms on data structures which may contain arbitrarily
deeply nested sub structures is a piece of cake:

```clojure
(ns examples.transform
  (:require [will08rien.prg :refer :all]
            [will08rien.prg.stdio :refer :all]))

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
```

Executing the above yields the following output:

```
$ cat examples/transform-input.json| clojure -M examples/transform.clj | jq .
{
  "quux": 3,
  "waldo": {
    "foo": 1
  },
  "bar": 2
}
```

## License

Copyright © 2020 Will

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
