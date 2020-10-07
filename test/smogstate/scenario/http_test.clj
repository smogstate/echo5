(ns smogstate.scenario.http-test
  (:require [smogstate.scenario.http :as h]
            [smogstate.scenario.util :as u]
            [clojure.pprint :as pp]
            [clojure.test :refer [deftest is]]))

(def test-scenario (u/json->map (slurp "test/resources/scenario01.json")))

(def test-request {:body {}
                   :headers {}
                   :query {}
                   :method "GET"
                   :destination "test.com"
                   :path "test.html"})

(deftest when-parse-json
  (pp/pprint test-scenario)
  (let [matchers (:matchers test-scenario)]
    (is (= 5
           (count matchers)))))

(deftest when-request-matches
  (let [result (h/matches? {:request test-request :scenario test-scenario})]
    (is (= true
           result))))
