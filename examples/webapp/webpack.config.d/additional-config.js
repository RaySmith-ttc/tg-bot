const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

config.module.rules.push({
    test: /\.m?js$/,
    resolve: {
        fullySpecified: false,
    },
});

const webpack = require("webpack");

config.plugins.push(new webpack.DefinePlugin({
    'process.env': JSON.stringify({}),
    'process.env.WEBAPP_GUARD_ENABLED':  JSON.stringify(process.env.WEBAPP_GUARD_ENABLED),
}));

// config.plugins.push(new BundleAnalyzerPlugin({
//     analyzerPort: 8888
// }));
