(ns smogstate.scenario.http)

(defn matches?
  "True if request matches scenario"
  [{:keys [request scenario]}]
  true)

