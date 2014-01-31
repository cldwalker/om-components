(ns om-components.sorted-table
  "Generates a table with sortable headers"
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-components.util :as util]))

(defn header-cell [owner header field]
  (dom/th
   #js {:onClick
        (fn [_]
          (let [sorted-fields (om/get-state owner :sorted-fields)
                ;; falsy is ascending, true is descending
                sort-state (get sorted-fields field)
                sort-fn (if sort-state
                          #(sort-by %1 (fn [a b] (compare b a)) %2)
                          #(sort-by %1 %2))]
            (om/set-state! owner
                           :sorted-fields (assoc sorted-fields field (not sort-state)))
            (om/set-state! owner
                           :rows (sort-fn field (om/get-state owner :rows)))))}
   header))

(defn sorted-table [app owner]
  (reify
    om/IInitState
    (init-state [_]
                {:sorted-fields {}})
    om/IRender
    (render [_]
            (util/render-table
             (om/get-state owner :rows)
             :header-cell (partial header-cell owner)))))
