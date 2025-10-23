# Screenshot API Tests

This directory contains unit tests for the screenshot API route located at `/pages/api/next/screenshot/[screenshot].ts`.

## Overview

The screenshot API route serves images from either:
1. Local filesystem (using `NEXT_PUBLIC_IMAGE_DIR` environment variable)
2. Remote server (using `NEXT_PUBLIC_SERVER_URL` environment variable)

## Test Coverage

The test suite provides **100% code coverage** and includes the following test categories:

### Local File Serving (3 tests)
- ✅ Serves existing screenshots from local filesystem
- ✅ Returns 500 error when file read fails
- ✅ Handles screenshots with special characters in filename

### Remote File Serving (2 tests)
- ✅ Fetches screenshots from remote server when file doesn't exist locally
- ✅ Handles axios errors when fetching remote screenshots

### Invalid Requests (4 tests)
- ✅ Returns 400 when screenshot parameter is missing
- ✅ Returns 400 when screenshot parameter is empty string
- ✅ Returns 400 when screenshot parameter is null
- ✅ Returns 400 when screenshot parameter is undefined

### Edge Cases & Security (5 tests)
- ✅ Handles array query parameters (returns 400)
- ✅ Rejects path traversal attempts with `..`
- ✅ Rejects filenames with forward slashes
- ✅ Rejects filenames with backslashes
- ✅ Handles very long filenames

### Environment Variable Handling (2 tests)
- ✅ Uses default path when `NEXT_PUBLIC_IMAGE_DIR` is not set
- ✅ Returns 400 when `NEXT_PUBLIC_SERVER_URL` is missing and file doesn't exist locally

### Content-Type and Headers (2 tests)
- ✅ Sets correct Content-Type header for PNG images
- ✅ Sets correct Content-Disposition header with filename

### File System Operations (2 tests)
- ✅ Correctly constructs file path from environment variable
- ✅ Handles case when `fs.existsSync` returns false

**Total: 20 tests with 100% coverage**

## Test Data

Test screenshots are located in `/data/test_screenshots/`:
- `facebook.com.png` - Primary test image
- `google.com.png` - Additional test image
- `github.com.png` - Additional test image

## Environment Variables

The tests mock the following environment variables:
- `NEXT_PUBLIC_IMAGE_DIR` - Set to `../data/test_screenshots/` for tests
- `NEXT_PUBLIC_SERVER_URL` - Set to `http://localhost:9000` for tests

## Running Tests

```bash
# Run all screenshot tests
pnpm test __tests__/api/screenshot/screenshot.test.tsx

# Run with coverage
pnpm coverage

# Run in watch mode
pnpm test -- __tests__/api/screenshot/screenshot.test.tsx --watch
```

## Bug Fixes

During the creation of these tests, the following bugs were discovered and fixed:

1. **Missing validation for screenshot parameter**: The handler didn't validate the `screenshot` parameter before using it in `path.join`, causing TypeErrors when the parameter was null, undefined, or an array. Added validation to return 400 for invalid parameters.

2. **Missing return statement in error handler**: The `fs.readFile` error callback didn't have a return statement after sending the 500 response, potentially causing issues. Added return statement.

3. **Incorrect SERVED_IMAGE check**: When `NEXT_PUBLIC_SERVER_URL` was undefined, `SERVED_IMAGE` became the string `"undefined/api/screenshots/"` which is truthy. Fixed to properly check for undefined and null values.

4. **Path traversal vulnerability**: The handler didn't prevent path traversal attacks. Added validation to reject filenames containing `..`, `/`, or `\` characters, preventing directory traversal attempts.

## Implementation Notes

- Tests use `vi.resetModules()` and dynamic imports to ensure environment variables are properly loaded for each test
- The handler uses async file operations, so tests use `vi.waitFor()` to handle timing
- Mocks are cleared after each test to prevent interference between tests
