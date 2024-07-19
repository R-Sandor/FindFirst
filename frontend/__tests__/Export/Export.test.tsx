import Navbar from "@components/Navbar/Navbar";
import authService, { User } from "@services/auth.service";
import { render } from "@testing-library/react";
import { beforeEach, describe, it, vi } from "vitest";

beforeEach(async () => {
  const user: User = { username: "jsmith", refreshToken: "blahblajhdfh34234" };
  // Mock GET request to /users when param `searchText` is 'John'
  // arguments for reply are (status, data, headers)
  vi.spyOn(authService, "getUser").mockImplementation(() => user);
  vi.spyOn(authService, "getAuthorized").mockImplementation(() => 1);
  render(<Navbar />);
});

describe("Test Export", () => {
  it("User can download bookmarks", () => {});
});
