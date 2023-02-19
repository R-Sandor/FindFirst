module.exports = {
  runtimeCompiler: true,
  transpileDependencies: true,
  devServer: {
    proxy: {
      '^/api': {
        target: 'http://localhost:9000',
        changeOrigin: true
      },
    }
  }
};
