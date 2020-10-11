(ns smogstate.scenario.http
  (:require
   [clojure.string :as s]
   [smogstate.matchers :as m]))

(def empty-value "")

(def matcher-suffix "-matcher")

(defn get-left-value
  ""
  [left {:keys [parameters]}]
  (if (map? left)
    (let [map-key (:name parameters)
          map-value (get left map-key empty-value)]
      map-value)
    (if (nil? left)
      empty-value
      left)))

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
    (fn[a] (get a name empty-value))
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
  (let [matchers (:request scenario)
        p {:instance instance}
        m (map #(match-block (assoc p :block %1)) matchers)]
    (reduce #(and %1 %2) true m)))

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
