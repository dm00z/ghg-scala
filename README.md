# We use:
+ scala.js

# Dev setup
1. install node.js
2. run:
```
npm install -g electron-prebuilt webpack webpack-dev-server
npm install
sbt ~fastOptJS
```

//npm start

```
webpack --watch
```

```
webpack-dev-server --progress --colors --port 8090
```

browse http://localhost:8090/

# Dist guide
```
electron .
```

# TODO
+ i18n
+ should we use [squants](http://www.squants.com/)?
+ use [formsy-react](https://github.com/christianalfoni/formsy-react) to validate inputs?
