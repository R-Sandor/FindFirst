import { SearchTypeChar } from "@components/Navbar/Searchbar";
import { act, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
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
import { bkmkResp } from "../../data/SampleData";
import { debug } from "vitest-preview";

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
    expect(searchTypeButton).toBeInTheDocument();

    const searchBar = screen.getByPlaceholderText(/search/i);

    await type(user, searchBar, "/");
    expect(searchBar).toHaveValue("/");

    await type(user, searchBar, "v");
    expect(searchBar).toHaveValue("/v");

    await backSpaceOnField(user, searchBar, 2);
    expect(searchBar).toHaveValue("");

    await type(user, searchBar, "/t");
    expect(searchTypeButton).toHaveTextContent("/t");
  });

  it("Switching type with Tag from Tag search to Tilte search creates tags to text.", async () => {
    mock.onGet().reply(200, data); // any search return the generic data.
    const searchTypeButton = screen.getByText(`/${SearchTypeChar[0]}`);
    const searchBar = screen.getByPlaceholderText(/search/i);
    await type(user, searchBar, "Golang is bad");
    await click(user, searchTypeButton);
    await click(user, searchTypeButton);
    await backSpaceOnField(user, searchBar, 1);
    let allTags = await screen.findAllByTestId(/tag-/);

    expect(allTags.length).toEqual(2);
    expect(searchBar.getAttribute("value")).toEqual("bad");

    // delete bad
    await backSpaceOnField(user, searchBar);
    // pop tag
    await backSpaceOnField(user, searchBar, 1);
    // delete is
    await backSpaceOnField(user, searchBar);
    // pop tag
    await backSpaceOnField(user, searchBar, 1);
    // delete Golang
    await backSpaceOnField(user, searchBar);

    allTags = screen.queryAllByTestId(/tag-/);
    expect(allTags.length).toEqual(0);

    await type(user, searchBar, "Golang");
    await type(user, searchBar, "{enter}");
  });

  it("User switch from tag search back to text", async () => {
    mock.onGet().reply(200, data); // any search return the generic data.
    const searchTypeButton = screen.getByText(`/${SearchTypeChar[0]}`);
    const searchBar = screen.getByPlaceholderText(/search/i);
    await click(user, searchTypeButton);
    await click(user, searchTypeButton);
    await type(user, searchBar, "Java ");
    await type(user, searchBar, "is ");
    await type(user, searchBar, "better ");
    let allTags = await screen.findAllByTestId(/tag-/);
    expect(allTags.length).toEqual(3);

    await click(user, searchTypeButton);

    expect(searchTypeButton).toHaveTextContent("/b");
    expect(searchBar.getAttribute("value")).toEqual("Java is better");
  });
});
