import { beforeEach, describe, expect, it, vi, vitest } from "vitest";
import { render, screen } from "@testing-library/react";
import { act } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { instance } from "@api/Api";
import MockAdapter from "axios-mock-adapter";
import { debug } from "vitest-preview";
import { bkmkResp, tagsData } from "../data/SampleData";
import Page from "@/app/page";
import { Navbar } from "react-bootstrap";
import authService, { User } from "@services/auth.service";
const userEvnt = userEvent.setup();

const data = JSON.stringify(bkmkResp, null, 2);

let mock: any;
beforeEach(async () => {
  const user: User = { username: "jsmith", refreshToken: "blahblajhdfh34234" };
  mock = new MockAdapter(instance);
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

describe("User Imports Bookmark", () => {
  it("File Picker Opens", async () => {
    let filePicker: any;
    await act(async () => {
      filePicker = screen.queryByTestId("bk-imprt-btn");
    });
    expect(filePicker).not.toEqual(null);
    await act(async () => {
      userEvnt.click(filePicker);
    });
    expect(screen.queryByText(/Import Bookmarks/i)).not.toEqual(null);
  });
  it("User uploads files", async () => {
    await act(async () => {
      userEvnt.click(screen.getByTestId("bk-imprt-btn"));
      userEvnt.click(screen.getByText(/Select/i));
    });
    debug();
  });
});
