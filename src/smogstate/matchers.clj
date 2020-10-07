(ns smogstate.matchers
  (:require [clojure.string :as s]))

(defn exact?
  "True if left is equals right"
  [left right]
  (= left right))

(defn starts-with?
  "True if left starts with right"
  [left right]
  (s/starts-with? left right))

(defn ends-with?
  "True if left ends with right"
  [left right]
  (s/ends-with? left right))

(defn includes?
  "True if left contains right"
  [left right]
  (s/includes? left right))
