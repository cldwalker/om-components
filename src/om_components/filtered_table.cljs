(ns om-components.filtered-table
  "Inspired by http://www.phpied.com/reactivetable-bookmarklet/"
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-components.util :as util]))


(defn filter-rows [rows query]
  (if query
    (filter
     (fn [row] (some #(-> (str %)(.indexOf query) (> -1))
                     (vals row)))
     rows)
    rows))

(defn filtered-table
  "Component that renders a table with an input field to filter its rows. Requires
  state with :rows set to a vector of maps."
  [app owner]
  (reify
    om/IRender
    (render [_]
            (dom/div
             nil
             ;; is one callback worth async-ifying?
             (dom/input
              #js {:onChange (fn [e]
                               (om/set-state! owner
                                              :query
                                              (.. e -target -value)))})

             (let [rows (om/get-state owner :rows)]
               (util/render-table
                (filter-rows rows (om/get-state owner :query))
                :table-attributes (om/get-state owner :table-attributes)))))))
