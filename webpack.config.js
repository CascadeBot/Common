const KotlinWebpackPlugin = require('@jetbrains/kotlin-webpack-plugin');

module.exports = {
    entry: 'cascade-common', // This tells webpack where to begin for bundling

    resolve: {
        modules: ['bin/build', 'node_modules', 'build/js/node_modules'], // Webpack will use this to look for anything that is required
    },

    output: {
        path: __dirname + '/bin/bundle', // This is where the bundle will go
        filename: 'cascade-common.js', // The bundle will be called vectron.js
    },

    mode: "development",
    target: "node",

    plugins: [
        //Step one - Create a test build
        new KotlinWebpackPlugin({
            src: __dirname,                                   // Build Everything
            output: 'bin/test',                               // Output to bin/test
            moduleName: 'cascade-common',                            // Will create vectron.js
            moduleKind: 'commonjs',                           // Create commonjs modules
            librariesAutoLookup: true,                        // Uses node_modules for libraries
            packagesContents: [require('./package.json')],    // Points to package.json for dependencies
        }),
        // Step two - Create a production build
        new KotlinWebpackPlugin({
            src: __dirname + '/src',                          // Build only what is in src
            output: 'bin/build',                              // Output to bin/build
            moduleName: 'cascade-common',                            // Create a file called vectron.js
            moduleKind: 'commonjs',                           // Create commonjs modules
            metaInfo: true,                                   // Include .meta.js files
            sourceMaps: true,                                 // Include Source mappings
            librariesAutoLookup: true,                        // Uses node_modules for libraries
            packagesContents: [require('./package.json')],    // Points to package.json for dependencies
        }),
    ],
};