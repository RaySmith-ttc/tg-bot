
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;

module.exports = env => {
    const webpack = require("webpack");
    config.plugins.push(new webpack.DefinePlugin({
        'process.env': JSON.stringify({}),
        'process.env.FOO': env.FOO,
    }));

    config.module.rules.push({
        test: /\.m?js$/i,
        resolve: {
            fullySpecified: false,
        },
    });

    // config.plugins.push(new BundleAnalyzerPlugin({
    //     analyzerPort: 8888
    // }));

    return config
};