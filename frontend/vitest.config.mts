import { defineConfig, configDefaults } from "vitest/config";
import react from "@vitejs/plugin-react";
import tsconfigPaths from "vite-tsconfig-paths";
import path from "path";

const defaults = configDefaults.coverage.exclude
  ? configDefaults.coverage.exclude
  : [];

export default defineConfig({
  plugins: [react(), tsconfigPaths()],
  css: {
    preprocessorOptions: {
      scss: {
        api: "modern-compiler",
      },
    },
  },
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
        "components/navigation-events.tsx",
        "*/**/index.js",
        "api",
        "app/layout.tsx",
        ...defaults,
      ],
      reporter: ["text", "lcov", "html"],
      reportOnFailure: true,
    },
    testTimeout: 90000, // 90 seconds to run a test.
    poolOptions: {
      vmThreads: {
        memoryLimit: "500mb",
      },
    },
    exclude: [...configDefaults.exclude, "tests_e2e/**"],
  },
  resolve: {
    alias: {
      "@components": path.resolve(__dirname, "./components/"),
    },
  },
});
