(ns advent-of-code-2019.day01
  (:require [clojure.java.io :as io]
            [fastmath.core :as m]))

(set! *unchecked-math* :warn-on-boxed)
(set! *warn-on-reflection* true)

(defn required-fuel
  ^long [^long in]
  (long (- (m/floor (/ in 3.0)) 2.0)))

(def fuel-data (map (comp required-fuel read-string)
                    (-> (io/resource "day01.txt")
                        (io/reader)
                        (line-seq))))

(defn required-fuel-total
  ^long [^long in]
  (if (pos? in)
    (+ in (required-fuel-total (required-fuel in)))
    0))

{:fuel (reduce + fuel-data)
 :total-fuel (reduce + (map required-fuel-total fuel-data))}
;; => {:fuel 3497998, :total-fuel 5244112}

;; tests
(required-fuel-total (required-fuel 14));; => 2
(required-fuel-total (required-fuel 1969));; => 966
(required-fuel-total (required-fuel 100756));; => 50346
