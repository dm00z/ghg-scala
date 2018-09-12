# We use:
+ scala.js

# Dev setup
1. install node.js and yarn
2. run:
```
yarn global add electron
yarn
sbt ~fastOptJS
```
Start development server:
```
yarn start
```

Browse http://localhost:8090/

# Dist guide
```
sbt fullOptJS
yarn build
yarn dist
```

# TODO
+ i18n
+ should we use [squants](http://www.squants.com/)?
+ use [formsy-react](https://github.com/christianalfoni/formsy-react) to validate inputs?
