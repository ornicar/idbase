Base de données et moteur de recherche de fiches pédagogiques
---

Simple application providing advanced search over a database.
Main language is french.

Uses Play 2.1, mongodb, and bootstrap.

### Installation

This is full-stack specific application, not a library, not a framework, and not a generic, multi-purpose "engine".
You are welcome to use it as a bootstrap for your own developments, but you *will need* to get your hands dirty and modify the source code to get it to do what you want.

> Prerequesites: unix, mongodb >= 2.4 with text search enabled

> In production you'll also probably want to use a reverse proxy such as nginx.

```sh
git clone git://github.com/ornicar/idbase
cd idbase
```

### Configuration

```sh
cp conf/application.conf.dist conf/application.conf
```

`application.conf` extends `base.conf` and can override any value.
Note that `application.conf` is excluded from git index.

### Run it

Idbase is a play2 project, so it uses sbt for the development workflow.

```
sbt play
> compile
> run
```

will start the application on port 9000.
