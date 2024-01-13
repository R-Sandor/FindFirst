import { expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import Page from "../app/page";
import { debug } from "vitest-preview";
import { describe } from "node:test";
import authService, { User } from "@services/auth.service";

describe("user not authenticated", () => {
  it("Page loads", () => {
    render(<Page />);
    expect(screen.getByText("Hello Welcome to findfirst.")).toBeDefined();
  });
});

describe("User is authenticated and bookmark/tag data is present.", () => {

  const user: User = {username: "jsmith", refreshToken: "blahblajhdfh34234"}
  vi.spyOn(authService, 'getUser').mockImplementation(() => user)

  it("should be bookmarks available", () => {
    render(<Page />);
    debug()
  });
});
