(ns om-components.sorted-table
  "Generates a table with sortable headers"
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-components.util :as util]))

(defn sorted-table [app owner]
  (reify
    om/IRender
    (render [_]
            (util/render-table
             (om/get-state owner :rows)
             :header-cell #(dom/th
                               #js {:onClick (fn [_]
                                               (prn "Sorted" %2)
                                               (om/set-state! owner
                                                              :rows
                                                              (sort-by %2 (om/get-state owner :rows))))} %1)))))
