(ns smogstate.scenario.http
  (:require
   [clojure.string :as s]
   [malli.core :as malli]
   [malli.error :as me]
   [smogstate.matchers :as m]))

(def StartsWithMatcher
  [:map
   [:name {:optional true} string?]
   [:starts-with-matcher string?]])

(def EndsWithMatcher
  [:map
   [:name {:optional true} string?]
   [:ends-with-matcher string?]])

(def IncludesMatcher
  [:map
   [:name {:optional true} string?]
   [:includes-matcher string?]])

(def ExactMatcher
  [:map
   [:name {:optional true} string?]
   [:exact-matcher string?]])

(def Matcher
  [:or
   StartsWithMatcher
   EndsWithMatcher
   IncludesMatcher
   ExactMatcher])

(def Scenario
  [:map
   [:request
    [:map
     [:destination {:optional true} [:vector Matcher]]
     [:method {:optional true} [:vector Matcher]]
     [:headers {:optional true} [:vector Matcher]]
     [:query {:optional true} [:vector Matcher]]
     [:path {:optional true} [:vector Matcher]]]]])

(def empty-value "")

(def matcher-suffix "-matcher")

(defn matcher-attr?
  "True if matcher attribute"
  [k]
  (m/ends-with? (name k) matcher-suffix))

(defn find-first
  [f coll]
  (first (filter f coll)))

(defn keyword->type
  ""
  [k]
  (when (nil? k)
    (throw (ex-info "keyword is nil" k)))
  (let [s (name k)
        index (s/index-of s matcher-suffix)]
    (keyword (subs s 0 index))))

(defn transform
  "Converts left to string"
  [{:keys [name]}]
  (if-not (nil? name)
    (fn [a] (get a name empty-value))
    identity))

(defn definition->matcher
  ""
  [m]
  (let [type-attr (find-first #(matcher-attr? %1) (keys m))
        value (type-attr m)
        parameters (dissoc m type-attr)
        transform (transform parameters)
        type (keyword->type type-attr)]
    {:type type :right value :transform transform}))

(defn left->string
  ""
  [{:keys [left transform] :as m}]
  (assoc m :left (transform left)))

(defn do-match
  ""
  [left matchers]
  (let [m (->> matchers
               (map definition->matcher)
               (map #(assoc %1 :left left))
               (map left->string)
               (map m/match))]
    (reduce #(and %1 %2) true m)))

(defn match-block
  ""
  [{:keys [instance block]}]
  (let [k (first block)
        matchers (second block)
        left (k instance)]
    (if-not (nil? left)
      (do-match left matchers)
      false)))

(defn matches?
  "True if request matches scenario"
  [{:keys [instance scenario]}]
  (println "scenario" scenario)
  (let [error (-> Scenario
                  (malli/explain scenario)
                  (me/humanize))]
    (if error
      (throw (ex-info "Scenario validation failed." error)))
    (let [matchers (:request scenario)
          p {:instance instance}
          m (map #(match-block (assoc p :block %1)) matchers)]
      (reduce #(and %1 %2) true m))))

(comment (matches? {:instance {:destination "test.org"
                               :path "/test.html"
                               :method "GET"
                               :headers {"support_id" "S01"
                                         "test_id" "T01"}}
                    :scenario
                    {:request
                     {:path [{:exact-matcher "/test.html"}],
                      :method [{:exact-matcher "GET"}],
                      :headers
                      [{:starts-with-matcher "S01", :name "support_id"}
                       {:name "test_id", :exact-matcher "T01"}],
                      :destination [{:exact-matcher "test.org"}]}}}))

(comment (keyword->type :exact-matcher))
