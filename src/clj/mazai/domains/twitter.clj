(ns mazai.domains.twitter)

(defn images [job]
  (or
   (get-in job [:raw :twitter_quoted_status :twitter_extended_entities :media])
   (get-in job [:raw :twitter_extended_entities :media])
   (get-in job [:raw :object :twitter_extended_entities :media])
   (get-in job [:raw :object :long_object :twitter_extended_entities :media])
   []))
