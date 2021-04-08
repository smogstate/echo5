(ns smogstate.scenario.http-test
  (:require [smogstate.scenario.http :as h]
            [smogstate.scenario.util :as u]
            [clojure.pprint :as pp]
            [clojure.test :refer [deftest is]]))

(def test-scenario (u/json->map (slurp "test/resources/scenario01.json")))

(def test-request {:body {}
                   :headers {"support_id" "S01"
                             "test_id" "T01"}
                   :query nil
                   :method "GET"
                   :destination "test.org"
                   :path "test.html"})

(pp/pprint test-scenario)

(deftest when-parse-json
  (pp/pprint test-scenario)
  (let [matchers (:request test-scenario)]
    (is (= 5
           (count matchers)))))

(deftest when-everything-ok-then-matches
  (let [result (h/matches? {:instance test-request
                            :scenario {:request
                                       {:destination [{:exact-matcher "test.org"}]
                                        :method [{:exact-matcher "GET"}]
                                        :path [{:starts-with-matcher "test"}]
                                        :headers [{:name "support_id" :exact-matcher "S01"}]}}})]
    (is (= true
           (:match result)))))

(deftest when-header-not-ok-then-dont-match
  (let [result (h/matches? {:instance test-request
                            :scenario {:request
                                       {:destination [{:exact-matcher "test.org"}]
                                        :method [{:exact-matcher "GET"}]
                                        :path [{:starts-with-matcher "test"}]
                                        :headers [{:name "support_id" :exact-matcher "S011"}]}}})]
    (is (= false
           (:match result)))))
