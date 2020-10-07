(ns smogstate.scenario.util
  (:require [jsonista.core :as j]))

(def read-mapper (j/object-mapper {:decode-key-fn true}))

(defn json->map
  ""
  [source]
  (j/read-value source read-mapper))
