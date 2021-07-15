const { createSSRApp } = require("vue");
const { renderToString } = require("@vue/server-renderer");
const cors = require('cors')
const express = require("express");
const manifest = require("../dist/ssr-manifest.json");
const path = require("path");
const api = require('./api');
const fs = require("fs");

const service = express();
const appPath = path.join(__dirname, "../dist", manifest["app.js"]);


service.get('/mpp-table/:fileName', cors(), async (req, res) => {
    api.getJsonData(req.params.fileName).then(json => {
        fs.writeFile(path.join(__dirname, "assets", "data.json"), JSON.stringify(json.data), async (wErr) => {
            if (wErr) {
                console.log(wErr);
                res.status(417).send("Service fail!");
            } else {
                const htmlString = await getHTMLString(appPath);
                res.send(htmlString);
            }
        });
    }).catch(jsonErr => {
        console.log(jsonErr);
        res.status(417).send("Service fail!");
    });

});

async function getHTMLString(appPath) {
    const App = require(appPath).default;
    const ssrApp = createSSRApp(App);
    const appContent = await renderToString(ssrApp);
    const cssString = fs.readFileSync(path.join(__dirname, '../dist', manifest["app.css"]), 'utf-8');
    const htmlString = `
            <style>${cssString}</style>
            ${appContent}
    `

    return htmlString;
}

service.listen(8081, () => {
    console.log(`
  You can navigate to http://localhost:8081
`);
});

