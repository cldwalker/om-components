(ns om-components.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-components.filtered-table :refer [filtered-table]]
            [om-components.sorted-table :refer [sorted-table]]))

(enable-console-print!)

(def app-state (atom {:text "Component Examples!"}))


(defn filtered [app owner]
  (reify
    om/IRender
    (render [_]
            (dom/div nil
                     (dom/h2 nil "Filtered Table")
                     (om/build filtered-table app
                               {:init-state
                                {:rows (mapv #(hash-map :id % :name (str "Name " (inc %)))
                                             (range 0 100))}})))) )

(defn sorted [app owner]
  (reify
    om/IRender
    (render [_]
            (dom/div nil
                     (dom/h2 nil "Sorted Table")
                     (om/build sorted-table app
                               {:init-state
                                {:rows (mapv #(hash-map :id % :name (str "Name " (inc %)))
                                             (range 0 20))}})))) )
(om/root
  app-state
  (fn [app owner]
    (dom/div
     nil
     (dom/h1 nil (:text app))
     (om/build sorted app)
     (om/build filtered app)))
  (. js/document (getElementById "app")))
