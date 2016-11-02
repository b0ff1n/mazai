# mazai

FIXME

## Development Environment

### Prerequisites

* Latest JDK 1.8
* Latest Leiningen (>= 2.5.0)

### Development Workflow

This service uses Stuart Sierra's [component](https://github.com/stuartsierra/component) library to wire different parts of the application. More information about this approach is [here](http://youtu.be/13cmHf_kt-Q).

* Start the REPL: `lein repl`
* Start the application inside REPL: `(reset)` \*
* In another terminal, run `bower install` to fetch js dependencies \*\*
* Start the ClojureScript compilation: `lein cljsbuild auto dev` \*\*\*
* Go to [localhost:8080](http://localhost:8080/) to see `Hello World!`

\* You will need to run `(reset)` every time you change backend clojure code.

\*\* You only have to run it if you change the js dependencies.

\*\*\* When you change your ClojureScript code, it'll be automatically recompiled, you will just need to refresh your browser.

## Configuration

Development configuration is defined in dev/user.clj. Production configuration is in etc/mazai.edn.tmpl.
