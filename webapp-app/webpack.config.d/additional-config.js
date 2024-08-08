// noinspection NpmUsedModulesInstalled,JSUnresolvedReference

module.exports = env => {
    const webpack = require("webpack");
    const environmentPlugin = new webpack.DefinePlugin({
        'process.env': JSON.stringify({}),
        'process.env.TG_BOT_TOKEN': JSON.stringify(env.TG_BOT_TOKEN),
    });
    config.plugins.push(environmentPlugin)

    return config
};
