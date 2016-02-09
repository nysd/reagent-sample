(ns docker.ui.routes 
  (:require
   [secretary.core :as secretary :refer-macros  [defroute]]
   [clojure.string :as string]
   [reagent.core :as reagent :refer  [atom]]
   [clojure.browser.repl :as repl]
   [docker.ui.views :as view]
   [ajax.core :as ajax]
   [cljs.reader :as reader]))

(enable-console-print!)

(defn handle-popstate  [event]
  (let [v (.-state event)]
    (js/console.log v)
    (view/set-current-view! view/stats-view)))

(.addEventListener js/window  "popstate" handle-popstate)

(defn- push-state 
  "push-stateする"
  [state title url]
  (.pushState js/history state title url))

(defroute "/containers/:id" {:as params}
  (js/console.log "dispatch")
  (let [handler #(view/set-current-view! (fn [] (view/container-view %)))]
    (ajax/GET (str "/api/containers/" (:id params))  {:handler handler :error-handler js/console.log}))
  (push-state params "Container Page" (str "/containers/" (:id params))))

(defroute "/index.html" {} 
  (push-state {} "Root Page" "/index.html")
  (view/set-current-view! view/stats-view ))