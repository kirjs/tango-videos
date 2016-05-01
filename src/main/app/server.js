var WebpackDevServer = require("webpack-dev-server");
var webpack = require("webpack");
var compiler = webpack(require('./webpack.config.js'));

var apiProxy = process.env.TANGO_VIDEOS_REST || 'http://localhost:8087/';
var port = process.env.TANGO_VIDEOS_PORT || '8084';

var server = new WebpackDevServer(compiler, {
    inline: true,
    colors: true,
    https: true,
    progress: true,
    displayErrorDetails: true,
    displayCached: true,
    contentBase: 'src',
    historyApiFallback: true,
    proxy: {
        '/api/*': {
            target: apiProxy
        }
    }
});


server.listen(port, "0.0.0.0", function () {
    console.log("Running on " + port);
});

