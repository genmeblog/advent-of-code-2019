(ns advent-of-code-2019.day02
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [fastmath.core :as m]))

(set! *unchecked-math* :warn-on-boxed)
(set! *warn-on-reflection* true)

(def program (mapv read-string
                   (-> (io/resource "day02.txt")
                       (slurp)
                       (s/split #","))))


(defn make-program
  [noun verb]
  (-> program
      (assoc 1 noun)
      (assoc 2 verb)))

(defn process
  [f program ^long iter]
  (assoc program (program (+ iter 3))
         (f (program (program (inc iter)))
            (program (program (+ iter 2))))))

(defn executor
  ([program] (executor program 0))
  ([program ^long iter]
   (condp = (program iter)
     1 (executor (process + program iter) (+ iter 4))
     2 (executor (process * program iter) (+ iter 4))
     99 program)))

;; tests
(executor [1 0 0 0 99]);; => [2 0 0 0 99]
(executor [2 3 0 3 99]);; => [2 3 0 6 99]
(executor [2 4 4 5 99 0]);; => [2 4 4 5 99 9801]
(executor [1 1 1 4 99 5 6 0 99]);; => [30 1 1 4 2 5 6 0 99]

(def executor-0 (comp first executor make-program))

(set! *unchecked-math* :true)

;; OBSERVED
;;
;; difference between consecutive nouns for given verb (any) is constant
;; verb is added to the difference
(def difference (let [r-verb (int (rand 100))]
                  (->> (range 100)
                       (map #(executor-0 % r-verb))
                       (partition 2 1)
                       (map #(apply - %))
                       (distinct)
                       (first)
                       (-))))
;; => 460800

;; check verb difference is the same after running a program
(every? true? (repeatedly 1000 #(let [a (int (rand 100))
                                      b (int (rand 100))]
                                  (= (- a b)
                                     (- (executor-0 33 a) (executor-0 33 b))))))
;; => true

(def offset (executor-0 0 0))
;; => 797908

(def target 19690720)
(def target- (- target offset))

(def noun (int (m/floor (/ target- difference))))
(def verb (mod target- difference))

;; check it
(executor-0 noun verb);; => 19690720

{:first-value (executor-0 12 2)
 :noun-verb (+ verb (* 100 noun))}
;; => {:first-value 6327510, :noun-verb 4112.0}
