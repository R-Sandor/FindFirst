import { SearchTypeChar } from "@components/Navbar/Searchbar";
import { act, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { debug } from "vitest-preview";
import { MockedFunction, beforeEach, describe, expect, it, vi } from "vitest";
import GlobalNavbar from "@components/Navbar/Navbar";
import {
  backSpaceOnField,
  click,
  type,
} from "@/__tests__/utilities/TestingUtilities";
import useAuth from "@components/UseAuth";
import { AuthStatus, User } from "@services/auth.service";
import { instance } from "@api/Api";
import { bkmkResp, tagsData } from "../../data/SampleData";

const user = userEvent.setup();
let mock: any;
const data = JSON.stringify(bkmkResp);
const urlB = "search/title";

const userAcc: User = { username: "jsmith", refreshToken: "blahblajhdfh34234" };

vi.mock("next/navigation", () => ({
  useRouter: vi.fn(),
}));

vi.mock("@components/UseAuth", () => ({
  default: vi.fn(),
}));

vi.mock(
  "@services/auth.service",
  async (
    importOriginal: () => Promise<typeof import("@services/auth.service")>,
  ) => {
    const actual = await importOriginal();
    return {
      __esModule: true,
      ...actual,
      default: {
        ...actual.default,
        logout: vi.fn(),
        getUser: vi.fn().mockImplementation(() => {
          return userAcc;
        }),
        AuthStatus: {
          Unauthorized: "Unauthorized",
          Authorized: "Authorized",
        },
      },
    };
  },
);

describe("Searchbar functionality tests", () => {
  (useAuth as MockedFunction<typeof useAuth>).mockReturnValue(
    AuthStatus.Authorized,
  );
  beforeEach(() => {
    let MockAdapter = require("axios-mock-adapter");

    mock = new MockAdapter(instance);
    act(() => {
      render(<GlobalNavbar />);
    });
    const urlF = new RegExp(`search/text*`);
    const urlT = new RegExp(`search/tag*`);
    mock
      .onGet(urlB)
      .reply(200, data)
      .onGet(urlF)
      .reply(200, data)
      .onGet(urlT)
      .reply(200, data);
  });

  it("Renders", () => {
    expect(screen.getByPlaceholderText(/search/i)).toBeInTheDocument();
  });

  it("Click toggle changes type", async () => {
    const searchTypeButton = screen.getByText(`/${SearchTypeChar[0]}`);
    expect(searchTypeButton).toBeInTheDocument();
    await click(user, searchTypeButton);
    expect(screen.getByText(`/${SearchTypeChar[1]}`)).toBeVisible();
    await click(user, searchTypeButton);
    expect(screen.getByText(`/${SearchTypeChar[2]}`)).toBeVisible();
  });

  it("Switching type with text from Title search to tag search creates tags from text.", async () => {
    const searchTypeButton = screen.getByText(`/${SearchTypeChar[0]}`);
    const searchBar = screen.getByPlaceholderText(/search/i);

    await type(user, searchBar, "Golang is bad");

    expect(searchTypeButton).toBeInTheDocument();

    await click(user, searchTypeButton);
    await click(user, searchTypeButton);

    let allTags = await screen.findAllByTestId(/tag-/);

    expect(allTags.length).toEqual(3);

    await click(user, screen.getByText("Golang"));

    allTags = await screen.findAllByTestId(/tag-/);

    expect(allTags.length).toEqual(2);
  });

  it("User changes type via searchbar with keyboard", async () => {
    mock.onGet().reply(200, data);
    const searchTypeButton = screen.getByText(`/${SearchTypeChar[0]}`);
    const searchBar = screen.getByPlaceholderText(/search/i);
    await type(user, searchBar, "/");

    expect(searchTypeButton).toBeInTheDocument();
    expect(searchBar).toHaveValue("/");

    await type(user, searchBar, "v");
    expect(searchBar).toHaveValue("/v");
    // fireEvent.keyDown(searchBar, { key: "Backspace" });

    await backSpaceOnField(searchBar, 2);
    // fireEvent.change(searchBar, { target: { value: "Hello Worl" } }); // Updates the input field's value
    expect(searchBar).toHaveValue("");
  });

  it(
    "Switching type with Tag from Tag search to Tilte search creates tags to text.",
  );
  it("User can click Tag to delete it from Navbar");

  it(
    "When user enters in white space after a tag it should work without an errors",
  );
});
