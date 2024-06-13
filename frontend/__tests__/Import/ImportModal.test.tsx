import { beforeEach, describe, expect, it, test, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import { act } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { debug } from "vitest-preview";
import authService, { User } from "@services/auth.service";
import Navbar from "@components/Navbar/Navbar";
const userEvnt = userEvent.setup();

beforeEach(async () => {
  const user: User = { username: "jsmith", refreshToken: "blahblajhdfh34234" };
  // Mock GET request to /users when param `searchText` is 'John'
  // arguments for reply are (status, data, headers)
  vi.spyOn(authService, "getUser").mockImplementation(() => user);
  vi.spyOn(authService, "getAuthorized").mockImplementation(() => 1);
  await act(async () => {
    render(
      <div>
        <Navbar />
      </div>,
    );
  });
});

describe("Import Bookmarks UI.", () => {
  it("UI renders correctly", async () => {
    await act(async () => {
      await userEvnt.click(screen.getByTestId("import-btn"));
    });
    expect(screen.getByText(/Import Bookmarks/i)).toBeInTheDocument();
    debug();
  });
});
