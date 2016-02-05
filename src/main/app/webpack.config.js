var webpack = require('webpack');
var path = require('path');

module.exports = {
  entry: {
    'app': './src/app.ts',
    'vendor': './src/vendor.ts'
  },
  output: {
    path:  __dirname + "/dist",
    filename: "bundle.js"
  },
  plugins: [
    new webpack.optimize.CommonsChunkPlugin('vendor', 'vendor.bundle.js')
  ],

  resolve: {
    extensions: ['', '.ts', '.js']
  },

  module: {
    loaders: [
      { test: /\.ts$/, loader: 'ts-loader' },
      { test: /\.css$/,   loader: 'raw-loader' },
      { test: /\.html$/,  loader: 'raw-loader' }
    ],
    noParse: [ path.join(__dirname, 'node_modules', 'angular2', 'bundles') ]
  },

  devServer: {
    historyApiFallback: true
  }
};
