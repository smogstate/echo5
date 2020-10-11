(ns smogstate.matchers
  (:require [clojure.string :as s]))

(defn exact?
  "True if left is equals right"
  [left right]
  (= (s/lower-case left)
     (s/lower-case right)))

(defn starts-with?
  "True if left starts with right"
  [left right]
  (s/starts-with? (s/lower-case left)
                  (s/lower-case right)))

(defn ends-with?
  "True if left ends with right"
  [left right]
  (s/ends-with? (s/lower-case left)
                (s/lower-case right)))

(defn includes?
  "True if left contains right"
  [left right]
  (s/includes? (s/lower-case left)
               (s/lower-case right)))

(defmulti match (fn [m] (:type m)))

(defmethod match :exact
  [{:keys [left right]}]
  (exact? left right))

(defmethod match :ends-with
  [{:keys [left right]}]
  (ends-with? left right))

(defmethod match :starts-with
  [{:keys [left right]}]
  (starts-with? left right))

(defmethod match :includes
  [{:keys [left right]}]
  (includes? left right))

(comment (match {:left "string" :type :exact :right "string"}))

(comment (match {:left "string" :type :ends-with :right "ing"}))

(comment (match {:left "string" :type :includes :right "trx"}))




