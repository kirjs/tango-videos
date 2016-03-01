var WebpackDevServer = require("webpack-dev-server");
var webpack = require("webpack");
var compiler = webpack(require('./webpack.config.js'));

var server = new WebpackDevServer(compiler, {
    inline: true,
    colors: true,
    https: true,na
    progress: true,
    displayErrorDetails: true,
    displayCached: true,
    contentBase: 'src',
    historyApiFallback: true,
    proxy: {
        '/api/*': {
            target: 'http://localhost:8087/'
        }
    }
});

var port = 8084;
server.listen(port, "0.0.0.0", function () {
    console.log("Running on " + port);
});

