import { defineConfig, configDefaults, UserConfig } from "vitest/config";
import react from "@vitejs/plugin-react";
import tsconfigPaths from "vite-tsconfig-paths";
import path from "path";

const defaults = configDefaults.coverage.exclude
  ? configDefaults.coverage.exclude
  : [];

export default defineConfig({
  plugins: [react(), tsconfigPaths()] as UserConfig["plugins"],
  test: {
    environment: "jsdom",
    globals: true,
    css: true,
    setupFiles: ["vitestSetup.ts"],
    coverage: {
      exclude: [
        "types/**",
        "*.config.js",
        "components/Logo.js",
        "*/**/index.js",
        "api",
        ...defaults,
      ],
      reporter: ["text", "lcov", "html"],
      reportOnFailure: true,
    },
    testTimeout: 60000, // 40 seconds to run a test.
    poolOptions: {
      vmThreads: {
        memoryLimit: "500mb",
      },
    },
  },
  resolve: {
    alias: {
      "@components": path.resolve(__dirname, "./components/"),
    },
  },
});
