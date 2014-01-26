(ns om-components.filtered-table
  "Inspired by http://www.phpied.com/reactivetable-bookmarklet/"
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [clojure.string :as string]))


(defn filter-rows [rows query]
  (if query
    (filter
     (fn [row] (some #(-> (str %)(.indexOf query) (> -1))
                     (vals row)))
     rows)
    rows))

(defn render-table [rows & {:keys [table-attributes]}]
  (let [fields (distinct (mapcat keys rows))
        headers (map (comp string/capitalize name) fields)
        rows (mapv (apply juxt fields) rows)]
    (dom/table
     (clj->js table-attributes)
     (dom/thead
      nil
      (apply dom/tr nil
             (map
              #(dom/th nil %)
              headers)))
     (apply dom/tbody
      nil
      (map
       (fn [row]
         (apply dom/tr
               nil
               (map #(dom/td nil %) row)))
       rows)))))

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
               (render-table
                (filter-rows rows (om/get-state owner :query))
                :table-attributes (om/get-state owner :table-attributes)))))))
