
const { createServer } = require("https");
const { parse } = require("url");
const dotenv = require("dotenv");
dotenv.config({ path: ".env.development" });
const next = require("next");
const fs = require("fs");
const port = 3000;
const dev = process.env.NODE_ENV !== "production";
const key = process.env.SSL_KEY_FILE;
const crt = process.env.SSL_CRT_FILE;

const app = next({ dev });
const handle = app.getRequestHandler();

const httpsOptions = {
    key: fs.readFileSync (key),
    cert: fs.readFileSync(crt)
};

app.prepare().then(() => {
    createServer(httpsOptions, (req, res) => {
        const parsedUrl = parse(req.url, true);
        handle(req, res, parsedUrl);
    }).listen(port, (err) => {
        if (err) throw err;
        console.log("ready - started server on url: https://localhost:" + port);
    });
});

