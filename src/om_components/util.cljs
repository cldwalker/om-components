(ns om-components.util
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [clojure.string :as string]))


(defn render-table [rows & {:keys [table-attributes header-cell]}]
  (let [fields (distinct (mapcat keys rows))
        headers (map (comp string/capitalize name) fields)
        _headers (zipmap headers fields)
        rows (mapv (apply juxt fields) rows)
        header-cell (or header-cell #(dom/th nil %1))]
    (dom/table
     (clj->js table-attributes)
     (dom/thead
      nil
      (apply dom/tr nil
             (map #(header-cell % (get _headers %)) headers)))
     (apply dom/tbody
      nil
      (map
       (fn [row]
         (apply dom/tr
               nil
               (map #(dom/td nil %) row)))
       rows)))))
