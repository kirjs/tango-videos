var WebpackDevServer = require("webpack-dev-server");
var webpack = require("webpack");
var compiler = webpack(require('./webpack.config.js'));

var server = new WebpackDevServer(compiler, {
    inline: true,
    colors: true,
    progress: true,
    displayErrorDetails: true,
    displayCached: true,
    contentBase: 'src',
    proxy: {
        '/api/*': {
            target: 'http://localhost:8080/'
        }
    }
});

var port = 8084;
server.listen(port, "localhost", function () {
    console.log("Running on " + port);
});

