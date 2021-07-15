# server-render-table

## Client render

#### Project setup
```
npm install
```

#### Compiles and hot-reloads for development
```
npm run serve
```

#### Compiles and minifies for production
```
npm run build
```

## Server render

#### Compiles and build for server render
```
npm run pressr
```

#### Config file bundle.js
- Go to file dist/js/bundle.js
- Replace the inside of the created function of App component by the following code:
```
delete require.cache[require.resolve('../../src/assets/data.json');
this.json = require('../../src/assets/data.json');
```

#### Run service

```
node src/index.js
```
