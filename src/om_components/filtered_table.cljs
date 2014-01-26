(ns om-components.filtered-table
  "Inspired by http://www.phpied.com/reactivetable-bookmarklet/"
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))


(defn item [app owner]
  (reify
    om/IRender
    (render [_]
            (dom/li nil
                    (:name app)))))

(defn filter-rows [rows query]
  (if query
    (filter
     #(-> (:name %) (.indexOf query) (> -1))
     rows)
    rows))

(defn filtered-list
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

             ;; Using build-all requires a cursor. Is it worth cargo-culting?
             (apply dom/ul nil
                    (om/build-all
                     item
                     (filter-rows
                      (om/to-cursor (om/get-state owner :rows))
                      (om/get-state owner :query))))))))
