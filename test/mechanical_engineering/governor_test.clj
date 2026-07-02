(ns mechanical-engineering.governor-test
  (:require [clojure.test :refer [deftest is testing]]
            [mechanical-engineering.store :as store]
            [mechanical-engineering.governor :as governor]))

(defn- fresh-store []
  (let [st (store/mem-store)]
    (store/register-project! st {:project-id "project-1" :name "Bracket Redesign"})
    st))

(deftest ok-on-clean-review
  (let [st (fresh-store)
        proposal {:op :review :effect :propose :confidence 0.9 :stake :low}
        v (governor/check {:project-id "project-1"} {} proposal st)]
    (is (:ok? v))
    (is (not (:hard? v)))
    (is (not (:escalate? v)))))

(deftest hard-on-unregistered-project
  (let [st (fresh-store)
        proposal {:op :review :effect :propose :confidence 0.9 :stake :low}
        v (governor/check {:project-id "no-such-project"} {} proposal st)]
    (is (:hard? v))
    (is (some #(= :no-project (:rule %)) (:violations v)))))

(deftest hard-on-no-actuation-violation
  (let [st (fresh-store)
        proposal {:op :review :effect :direct-write :confidence 0.9 :stake :low}
        v (governor/check {:project-id "project-1"} {} proposal st)]
    (is (:hard? v))
    (is (some #(= :no-actuation (:rule %)) (:violations v)))))

(deftest escalates-on-load-bearing-sign-off
  (let [st (fresh-store)
        proposal {:op :sign-off-load-bearing-change :effect :propose :confidence 0.9 :stake :high}
        v (governor/check {:project-id "project-1"} {} proposal st)]
    (is (:escalate? v))
    (is (not (:hard? v)))))

(deftest escalates-on-pressure-vessel-certification
  (let [st (fresh-store)
        proposal {:op :certify-pressure-vessel-safety :effect :propose :confidence 0.9 :stake :high}
        v (governor/check {:project-id "project-1"} {} proposal st)]
    (is (:escalate? v))
    (is (not (:hard? v)))))

(deftest escalates-on-low-confidence
  (let [st (fresh-store)
        proposal {:op :review :effect :propose :confidence 0.2 :stake :low}
        v (governor/check {:project-id "project-1"} {} proposal st)]
    (is (:escalate? v))
    (is (not (:hard? v)))))

(deftest store-records-and-ledger-append-only
  (let [st (fresh-store)]
    (store/commit-record! st {:project-id "project-1" :op :design})
    (store/append-ledger! st {:disposition :commit})
    (is (= 1 (count (store/records-of st "project-1"))))
    (is (= 1 (count (store/ledger st))))))
