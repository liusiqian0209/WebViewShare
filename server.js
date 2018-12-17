const http = require('http');
const https = require('https');
const url = require('url');
const fs = require('fs');
const path = require('path');

const hostname = '192.168.20.187';
const httpPort = 3000;
const httpsPort = 3001;

const options = {
  key: fs.readFileSync(path.join(__dirname, './certs/private.pem')),
  cert: fs.readFileSync(path.join(__dirname, './certs/ca.cer'))
}

const httpServer = http.createServer((req, res) => {
  log('HTTP server -- received request: ' + req.url);
  const parsedUrl = url.parse(req.url, true, true);

  process(parsedUrl, res);
  
});

httpServer.listen(httpPort, hostname, () => {
  log(`Server running at http://${hostname}:${httpPort}/`);
});

const httpsServer = https.createServer(options, function(req, res) {
  log('HTTPS server -- received request: ' + req.url);
  const parsedUrl = url.parse(req.url, true, true);

  process(parsedUrl, res);
});

httpsServer.listen(httpsPort, hostname, () => {
  log(`Server running at https://${hostname}:${httpsPort}/`);
});

function process ( parsedUrl, res ){
  var filePath = path.join(__dirname, parsedUrl.pathname);
  if( fs.existsSync(filePath) && parsedUrl.pathname != "/") {
    var fileContent = fs.readFileSync(filePath);

    res.statusCode = 200;
    res.write(fileContent);

    log(parsedUrl.pathname + ' loaded');
  } else{
    res.statusCode = 404;
    res.write('404 Not Found!\n\n'+parsedUrl.pathname);
  }
  
  res.end();
}

function log( str ){
  console.log(str);
}