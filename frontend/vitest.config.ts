import { defineConfig, configDefaults } from 'vitest/config'
import react from '@vitejs/plugin-react'
import tsconfigPaths from 'vite-tsconfig-paths'
import path from 'path'

const defaults =  
       configDefaults.coverage.exclude? configDefaults.coverage.exclude: []
 
export default defineConfig({
  plugins: [react(), tsconfigPaths()],
  test: {
    environment: 'jsdom',
    globals: true,
    css: true,
    setupFiles: ['vitestSetup.ts'],
    coverage: {
       exclude: [
        'types/**',
        '*.config.js',
        '*/**/index.js',
        ...defaults,
        ]
     }
  },
  resolve: {
    alias: {
      '@components': path.resolve(__dirname, './components/'),
    },
  },
})