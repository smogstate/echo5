(ns smogstate.matchers-test
  (:require [smogstate.matchers :as m]
            [clojure.test :refer [deftest is]]))

(deftest when-exact
  (let [result (m/exact? "string" "string")]
    (is (= true
           result))))

(deftest when-starts-with
  (let [result (m/starts-with? "string" "str")]
    (is (= true
           result))))

(deftest when-ends-with
  (let [result (m/ends-with? "string" "ing")]
    (is (= true
           result))))

(deftest when-includes
  (let [result (m/includes? "string" "tr")]
    (is (= true
           result))))

