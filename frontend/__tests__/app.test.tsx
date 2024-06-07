import { beforeEach, describe, expect, test, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import Page from "../app/page";
import authService, { User } from "@services/auth.service";
import { instance } from "@api/Api";
import { bkmkResp, tagsData } from "./data/SampleData";
import userEvent from "@testing-library/user-event";
import { act } from "react-dom/test-utils";
import Navbar from "@components/Navbar/Navbar";
const userEvnt = userEvent.setup();

const data = JSON.stringify(bkmkResp, null, 2);

beforeEach(async () => {
  const user: User = { username: "jsmith", refreshToken: "blahblajhdfh34234" };
  let MockAdapter = require("axios-mock-adapter");
  let mock = new MockAdapter(instance);
  // Mock GET request to /users when param `searchText` is 'John'
  // arguments for reply are (status, data, headers)
  mock.onGet("/bookmarks").reply(200, data);
  mock.onGet("/tags").reply(200, JSON.stringify(tagsData));
  vi.spyOn(authService, "getUser").mockImplementation(() => user);
  vi.spyOn(authService, "getAuthorized").mockImplementation(() => 1);
  await act(async () => {
    render(
      <div>
        <Navbar />
        <Page />
      </div>,
    );
  });
});

describe("User is authenticated and bookmark/tag data is present.", () => {
  test("should be bookmarks available", async () => {
    const bkmkCard = await screen.findByText(/Best Cheesecake/i, undefined, {
      timeout: 1000,
    });
    expect(bkmkCard).toBeInTheDocument();
  });

  test("User clicks a tag", async () => {
    await act(async () => {
      await userEvnt.click(screen.getByTestId("deserts-list-item"));
    });
    const bkmkCard = screen.getByText(/Best Cheesecake/i);
    expect(bkmkCard).toBeInTheDocument();
  });
});

describe("Clicks around the page", () => {
  test("Change theme", async () => {
    await act(async () => {
      const toggle = screen.getByTestId("light-dark");
      await userEvnt.click(toggle);
    });
    expect(localStorage.getItem("theme")).toBe("light");
    const toggle = screen.getByTestId("light-dark");
    await act(async () => {
      await userEvnt.click(toggle);
    });
    expect(localStorage.getItem("theme")).toBe("dark");
  });
});
