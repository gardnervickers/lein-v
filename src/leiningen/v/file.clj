(ns leiningen.v.file
  (:require [clojure.string :as string]))

(defn- cache-source
  "Return Clojure source code for the version cache file"
  [version describe ns-prefix]
  (string/join "\n" [";; This code was automatically generated by the 'lein-v' plugin"
                     (if ns-prefix
                       (format "(ns %s.version)" ns-prefix)
                       "(ns version)")
                     (format "(def version \"%s\")" version)
                     (format "(def raw-version \"%s\")" describe)
                     ""]))

(defn version
  "Peek into the source of the project to read the cached version"
  [file]
  (try
    (load-file file)
    (eval 'version/version)
    (catch Exception _)))

(defn cache
  "Write the version of the given Leiningen project to a file-backed cache"
  [path version describe ns-prefix]
  (spit path (cache-source version describe ns-prefix)))
