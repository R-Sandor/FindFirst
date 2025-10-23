import { beforeEach, describe, expect, test, vi, afterEach } from "vitest";
import { NextApiRequest, NextApiResponse } from "next";
import axios from "axios";
import fs from "node:fs";
import path from "node:path";

// Mock axios
vi.mock("axios");

describe("Screenshot API Route", () => {
  let req: Partial<NextApiRequest>;
  let res: Partial<NextApiResponse>;
  let statusMock: any;
  let jsonMock: any;
  let sendMock: any;
  let setHeaderMock: any;
  let handler: any;

  beforeEach(async () => {
    // Setup environment variable to use test screenshots BEFORE importing the handler
    process.env.NEXT_PUBLIC_IMAGE_DIR = "../data/test_screenshots/";
    process.env.NEXT_PUBLIC_SERVER_URL = "http://localhost:9000";

    // Dynamically import the handler after setting env vars
    // This ensures the constants are evaluated with our test env vars
    const module = await import(
      "../../../pages/api/next/screenshot/[screenshot]"
    );
    handler = module.default;

    // Create mock response object
    statusMock = vi.fn().mockReturnThis();
    jsonMock = vi.fn().mockReturnThis();
    sendMock = vi.fn().mockReturnThis();
    setHeaderMock = vi.fn().mockReturnThis();

    res = {
      status: statusMock,
      json: jsonMock,
      send: sendMock,
      setHeader: setHeaderMock,
    };

    // Create mock request object
    req = {
      query: {},
    };
  });

  afterEach(() => {
    vi.clearAllMocks();
    // Clear module cache to ensure fresh imports
    vi.resetModules();
  });

  describe("Local file serving", () => {
    test("should serve an existing screenshot from local filesystem", async () => {
      req.query = { screenshot: "facebook.com.png" };

      await handler(req as NextApiRequest, res as NextApiResponse);

      // Wait for async file read to complete
      await vi.waitFor(
        () => {
          expect(setHeaderMock).toHaveBeenCalledWith(
            "Content-Type",
            "image/png",
          );
          expect(setHeaderMock).toHaveBeenCalledWith(
            "Content-Disposition",
            "attachment; filename=facebook.com.png",
          );
          expect(statusMock).toHaveBeenCalledWith(200);
          expect(sendMock).toHaveBeenCalled();
        },
        { timeout: 2000 },
      );
    });

    test("should return 500 if file read fails", async () => {
      req.query = { screenshot: "facebook.com.png" };

      // Mock fs.readFile to simulate an error
      const readFileSpy = vi.spyOn(fs, "readFile");
      readFileSpy.mockImplementation((_path: any, callback: any) => {
        callback(new Error("File read error"), null);
      });

      await handler(req as NextApiRequest, res as NextApiResponse);

      await vi.waitFor(
        () => {
          expect(statusMock).toHaveBeenCalledWith(500);
          expect(jsonMock).toHaveBeenCalledWith({
            error: "Unable to read file",
          });
        },
        { timeout: 2000 },
      );

      readFileSpy.mockRestore();
    });

    test("should handle screenshot with special characters in filename", async () => {
      // Create a test file with special characters
      const testFilename = "test-site.example.com.png";
      const testPath = path.join(
        process.cwd(),
        "../data/test_screenshots/",
        testFilename,
      );

      // Create the file temporarily for testing
      if (!fs.existsSync(testPath)) {
        fs.writeFileSync(testPath, Buffer.from("fake image data"));
      }

      req.query = { screenshot: testFilename };

      await handler(req as NextApiRequest, res as NextApiResponse);

      await vi.waitFor(
        () => {
          expect(setHeaderMock).toHaveBeenCalledWith(
            "Content-Type",
            "image/png",
          );
          expect(setHeaderMock).toHaveBeenCalledWith(
            "Content-Disposition",
            `attachment; filename=${testFilename}`,
          );
          expect(statusMock).toHaveBeenCalledWith(200);
        },
        { timeout: 2000 },
      );

      // Clean up
      if (fs.existsSync(testPath)) {
        fs.unlinkSync(testPath);
      }
    });
  });

  describe("Remote file serving", () => {
    test("should fetch screenshot from remote server when file does not exist locally", async () => {
      req.query = { screenshot: "nonexistent.png" };

      const mockImageData = Buffer.from("remote image data");
      (axios.get as any).mockResolvedValue({ data: mockImageData });

      await handler(req as NextApiRequest, res as NextApiResponse);

      await vi.waitFor(
        () => {
          expect(axios.get).toHaveBeenCalledWith(
            "http://localhost:9000/api/screenshots/nonexistent.png",
          );
          expect(statusMock).toHaveBeenCalledWith(200);
          expect(sendMock).toHaveBeenCalled();
        },
        { timeout: 2000 },
      );
    });

    test("should handle axios errors when fetching remote screenshot", async () => {
      req.query = { screenshot: "error.png" };

      (axios.get as any).mockRejectedValue(new Error("Network error"));

      await expect(
        handler(req as NextApiRequest, res as NextApiResponse),
      ).rejects.toThrow("Network error");
    });
  });

  describe("Invalid requests", () => {
    test("should return 400 when screenshot parameter is missing", async () => {
      req.query = {};

      await handler(req as NextApiRequest, res as NextApiResponse);

      expect(statusMock).toHaveBeenCalledWith(400);
      expect(sendMock).toHaveBeenCalledWith("No file");
    });

    test("should return 400 when screenshot parameter is empty string", async () => {
      req.query = { screenshot: "" };

      await handler(req as NextApiRequest, res as NextApiResponse);

      expect(statusMock).toHaveBeenCalledWith(400);
      expect(sendMock).toHaveBeenCalledWith("No file");
    });

    test("should return 400 when screenshot parameter is null", async () => {
      req.query = { screenshot: null };

      await handler(req as NextApiRequest, res as NextApiResponse);

      expect(statusMock).toHaveBeenCalledWith(400);
      expect(sendMock).toHaveBeenCalledWith("No file");
    });

    test("should return 400 when screenshot parameter is undefined", async () => {
      req.query = { screenshot: undefined };

      await handler(req as NextApiRequest, res as NextApiResponse);

      expect(statusMock).toHaveBeenCalledWith(400);
      expect(sendMock).toHaveBeenCalledWith("No file");
    });
  });

  describe("Edge cases", () => {
    test("should handle array query parameter (return 400)", async () => {
      req.query = { screenshot: ["file1.png", "file2.png"] };

      await handler(req as NextApiRequest, res as NextApiResponse);

      // Should return 400 for array parameter
      expect(statusMock).toHaveBeenCalledWith(400);
      expect(sendMock).toHaveBeenCalledWith("No file");
    });

    test("should reject path traversal attempts with ..", async () => {
      req.query = { screenshot: "../../../etc/passwd" };

      await handler(req as NextApiRequest, res as NextApiResponse);

      // Should return 400 for path traversal
      expect(statusMock).toHaveBeenCalledWith(400);
      expect(sendMock).toHaveBeenCalledWith("Invalid filename");
    });

    test("should reject filenames with forward slashes", async () => {
      req.query = { screenshot: "subdir/file.png" };

      await handler(req as NextApiRequest, res as NextApiResponse);

      // Should return 400 for paths with slashes
      expect(statusMock).toHaveBeenCalledWith(400);
      expect(sendMock).toHaveBeenCalledWith("Invalid filename");
    });

    test("should reject filenames with backslashes", async () => {
      req.query = { screenshot: String.raw`subdir\file.png` };

      await handler(req as NextApiRequest, res as NextApiResponse);

      // Should return 400 for paths with backslashes
      expect(statusMock).toHaveBeenCalledWith(400);
      expect(sendMock).toHaveBeenCalledWith("Invalid filename");
    });

    test("should handle very long filenames", async () => {
      const longFilename = "a".repeat(255) + ".png";
      req.query = { screenshot: longFilename };

      const mockImageData = { data: "data" };
      (axios.get as any).mockResolvedValue(mockImageData);

      await handler(req as NextApiRequest, res as NextApiResponse);

      await vi.waitFor(
        () => {
          expect(statusMock).toHaveBeenCalled();
        },
        { timeout: 2000 },
      );
    });
  });

  describe("Environment variable handling", () => {
    test("should use default path when NEXT_PUBLIC_IMAGE_DIR is not set", async () => {
      delete process.env.NEXT_PUBLIC_IMAGE_DIR;
      process.env.NEXT_PUBLIC_SERVER_URL = "http://localhost:9000";

      // Reload handler with new env
      vi.resetModules();
      const module = await import(
        "../../../pages/api/next/screenshot/[screenshot]"
      );
      const handlerWithDefaultPath = module.default;

      req.query = { screenshot: "test.png" };

      const mockImageData = { data: "data" };
      (axios.get as any).mockResolvedValue(mockImageData);

      await handlerWithDefaultPath(
        req as NextApiRequest,
        res as NextApiResponse,
      );

      await vi.waitFor(
        () => {
          expect(statusMock).toHaveBeenCalled();
        },
        { timeout: 2000 },
      );
    });

    test("should handle missing NEXT_PUBLIC_SERVER_URL", async () => {
      process.env.NEXT_PUBLIC_IMAGE_DIR = "../data/test_screenshots/";
      delete process.env.NEXT_PUBLIC_SERVER_URL;

      // Reload handler with new env
      vi.resetModules();
      const module = await import(
        "../../../pages/api/next/screenshot/[screenshot]"
      );
      const handlerWithoutServerUrl = module.default;

      req.query = { screenshot: "nonexistent.png" };

      await handlerWithoutServerUrl(
        req as NextApiRequest,
        res as NextApiResponse,
      );

      await vi.waitFor(
        () => {
          expect(statusMock).toHaveBeenCalledWith(400);
          expect(sendMock).toHaveBeenCalledWith("No file");
        },
        { timeout: 2000 },
      );
    });
  });

  describe("Content-Type and headers", () => {
    test("should set correct Content-Type header for PNG images", async () => {
      req.query = { screenshot: "facebook.com.png" };

      await handler(req as NextApiRequest, res as NextApiResponse);

      await vi.waitFor(
        () => {
          expect(setHeaderMock).toHaveBeenCalledWith(
            "Content-Type",
            "image/png",
          );
        },
        { timeout: 2000 },
      );
    });

    test("should set correct Content-Disposition header with filename", async () => {
      const filename = "facebook.com.png";
      req.query = { screenshot: filename };

      await handler(req as NextApiRequest, res as NextApiResponse);

      await vi.waitFor(
        () => {
          expect(setHeaderMock).toHaveBeenCalledWith(
            "Content-Disposition",
            `attachment; filename=${filename}`,
          );
        },
        { timeout: 2000 },
      );
    });
  });

  describe("File system operations", () => {
    test("should correctly construct file path from environment variable", async () => {
      const expectedPath = path.join(
        process.cwd(),
        "../data/test_screenshots/",
        "facebook.com.png",
      );

      req.query = { screenshot: "facebook.com.png" };

      const existsSpy = vi.spyOn(fs, "existsSync");

      await handler(req as NextApiRequest, res as NextApiResponse);

      await vi.waitFor(
        () => {
          expect(existsSpy).toHaveBeenCalledWith(expectedPath);
        },
        { timeout: 2000 },
      );

      existsSpy.mockRestore();
    });

    test("should handle case when fs.existsSync returns false", async () => {
      req.query = { screenshot: "nonexistent-file.png" };

      const mockImageData = { data: "remote data" };
      (axios.get as any).mockResolvedValue(mockImageData);

      await handler(req as NextApiRequest, res as NextApiResponse);

      await vi.waitFor(
        () => {
          expect(axios.get).toHaveBeenCalled();
          expect(statusMock).toHaveBeenCalledWith(200);
        },
        { timeout: 2000 },
      );
    });
  });
});
