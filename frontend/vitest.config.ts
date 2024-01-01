import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'
import tsconfigPaths from 'vite-tsconfig-paths'
import path from 'path'
 
export default defineConfig({
  plugins: [react(), tsconfigPaths()],
  test: {
    environment: 'jsdom',
    globals: true,
    css: true,
    setupFiles: ["./vitestSetup.ts"],
  },
  resolve: {
    alias: {
      '@components': path.resolve(__dirname, './components/'),
    },
  },
})