import { beforeEach, describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import { act } from "@testing-library/react";
import NewBookmarkCard from "@components/Bookmark/NewBookmarkCard";
import userEvent from "@testing-library/user-event";
import MockAdapter from "axios-mock-adapter";
import { TagReqPayload } from "@type/Bookmarks/Tag";
import { instance } from "@api/Api";
import Bookmark from "@type/Bookmarks/Bookmark";
import { hitEnter, hitKey } from "../utilities/fireEvents";
import { populateTags } from "../utilities/BookmarkUtils/BookmarkUtil";
const user = userEvent.setup();

describe("New Bookmark Card Renders", () => {
  beforeEach(() => {
    render(
      <div data-bs-theme="dark" className="row pt-3">
        <div className="col-6 col-sm-12 col-md-12 col-lg-4">
          <NewBookmarkCard />
        </div>
      </div>,
    );
  });

  it("Card renders", () => {
    expect(screen.getByText("Add Bookmark")).toBeDefined();
    expect(screen.getByPlaceholderText(/discover/i)).toBeDefined();
    expect(screen.getByPlaceholderText("Enter a tag")).toBeDefined();
    expect(screen.getByText("Reset")).toBeDefined();
    expect(screen.getByText("Submit")).toBeDefined();
  });
});

describe("Fields logic", () => {
  beforeEach(() => {
    act(() => {
      render(
        <div data-bs-theme="dark" className="row pt-3">
          <div className="col-6 col-sm-12 col-md-12 col-lg-4">
            <NewBookmarkCard />
          </div>
        </div>,
      );
    });
  });

  it("No fields have data, should be disabled", () => {
    const submit = screen.getByText("Submit");
    expect(submit).toBeDisabled();
  });

  it("All Required fields are given data and submitted", async () => {
    const submit = screen.getByText("Submit");
    const tags = screen.getByPlaceholderText("Enter a tag");
    const url = screen.getByPlaceholderText(/discover/i);
    await act(async () => {
      await user.type(url, "foodnetwork.com");
      await user.type(tags, "cooking");
    });
    hitEnter(tags);
    expect(submit).not.toBeDisabled();

    // fields should be populated
    expect(url).toHaveValue("https://foodnetwork.com");

    const axiosMock = new MockAdapter(instance);
    const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL;
    const tagsAPI = SERVER_URL + "/api/tags";
    const bookmarkAPI = SERVER_URL + "/api/bookmark";

    const expectedResult: TagReqPayload[] = [
      {
        id: 1,
        title: "cooking",
        bookmarks: [],
      },
    ];

    const expectedBookmark: Bookmark = {
      id: 1,
      title: "foodnetwork.com",
      url: "foodnetwork.com",
      tags: [
        {
          id: 1,
          title: "cooking",
        },
      ],
      screenshotUrl: "",
      scrapable: false,
    };

    axiosMock.onPost(tagsAPI, ["cooking"]).reply(() => {
      return [200, JSON.stringify(expectedResult)];
    });

    axiosMock
      .onPost(bookmarkAPI, {
        title: "https://foodnetwork.com",
        url: "https://foodnetwork.com",
        tagIds: [1],
      })
      .reply(() => {
        return [200, JSON.stringify(expectedBookmark)];
      });

    await act(async () => {
      await user.click(submit);
    });

    // if everything submitted correctly then it should be empty input field.
    expect(url).toHaveValue("");
    expect(tags).toHaveValue("");
    expect(submit).toBeDisabled();
  });

  it("Valid Domains", async () => {
    async function checkDomain(
      url: string,
      submit: HTMLElement,
      isValid: boolean,
    ) {
      await act(async () => {
        await user.type(screen.getByPlaceholderText(/discover/i), url);
      });
      isValid ? expect(submit).toBeEnabled() : expect(submit).toBeDisabled();
      await act(async () => {
        await user.click(screen.getByText(/reset/i));
      });
    }
    const submit = screen.getByText("Submit");
    await checkDomain("facebook.whatever", submit, false);
    await checkDomain("findfirst.dev/discover", submit, true);
    await checkDomain("findfirst.dev/", submit, true);
    await checkDomain("findfirst.dev", submit, true);
    await checkDomain("sub.findfirst.dev", submit, true);
    await checkDomain("http://sub.findfirst.dev", submit, true);
    await checkDomain("https://sub.findfirst.dev", submit, true);
    await checkDomain("https://sub.findfirst/dev", submit, false);
    await checkDomain("sub/findfirst/dev", submit, false);
    await checkDomain("findfirst.dev/discover/blog.9.6.24", submit, true);
    await checkDomain("findfirst.dev/discover/blog.9.6.24.html", submit, true);
  });

  it("Reset option works", async () => {
    const reset = screen.getByText(/reset/i);
    const url = screen.getByPlaceholderText(/discover/i);
    const tags = screen.getByPlaceholderText("Enter a tag");
    const submit = screen.getByText("Submit");
    await act(async () => {
      await populateTags(
        [
          "cooking",
          "food",
          "recipes",
          "ideas",
          "home",
          "meals",
          "dinner",
          "favs2",
          "misc",
        ],
        user,
      );
      await user.type(url, "foodnetwork.com");
      await user.click(reset);
    });
    expect(url).toHaveValue("");
    expect(tags).toHaveValue("");
    expect(submit).toBeDisabled();
  });

  it("Field with unsubmitted tag", async () => {
    const submit = screen.getByText("Submit");
    const tags = screen.getByPlaceholderText("Enter a tag");
    const url = screen.getByPlaceholderText(/discover/i);
    await act(async () => {
      await user.type(url, "foodnetwork.com");
      await user.type(tags, "cooking");
      hitEnter(tags);
      await user.clear(tags);
      await user.type(tags, "food");
    });
    expect(submit).not.toBeDisabled();

    // fields should be populated
    expect(url).toHaveValue("https://foodnetwork.com");

    const axiosMock = new MockAdapter(instance);
    const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL;
    const tagsAPI = SERVER_URL + "/api/tags";
    const bookmarkAPI = SERVER_URL + "/api/bookmark";

    const expectedResult: TagReqPayload[] = [
      {
        id: 1,
        title: "cooking",
        bookmarks: [],
      },
      {
        id: 2,
        title: "food",
        bookmarks: [],
      },
    ];

    const expectedBookmark: Bookmark = {
      id: 1,
      title: "https://foodnetwork.com",
      url: "https://foodnetwork.com",
      tags: [
        {
          id: 1,
          title: "cooking",
        },
        {
          id: 2,
          title: "food",
        },
      ],
      screenshotUrl: "",
      scrapable: false,
    };

    axiosMock.onPost(tagsAPI, ["cooking", "food"]).reply(() => {
      return [200, JSON.stringify(expectedResult)];
    });

    axiosMock
      .onPost(bookmarkAPI, {
        title: "https://foodnetwork.com",
        url: "https://foodnetwork.com",
        tagIds: [1, 2],
      })
      .reply(() => {
        return [200, JSON.stringify(expectedBookmark)];
      });

    await act(async () => {
      await act(async () => {
        await user.click(submit);
      });
    });

    // if everything submitted correctly then it should be empty input field.
    expect(url).toHaveValue("");
    expect(tags).toHaveValue("");
    expect(submit).toBeDisabled();
  });
});

describe("Tags Operations", () => {
  beforeEach(() => {
    act(() => {
      render(
        <div data-bs-theme="dark" className="row pt-3">
          <div className="col-6 col-sm-12 col-md-12 col-lg-4">
            <NewBookmarkCard />
          </div>
        </div>,
      );
    });
  });

  it("Too many tags, UI should limit to 8 tags", async () => {
    await act(async () => {
      await populateTags(
        [
          "cooking",
          "food",
          "recipes",
          "ideas",
          "home",
          "meals",
          "dinner",
          "favs",
          "misc",
        ],
        user,
      );
    });
    expect(screen.getByText(/Too many tags/i)).toBeVisible();
  });

  it("Click delete a Tag", async () => {
    await act(async () => {
      await populateTags(["Tag1", "Tag2"], user);
      await user.click(screen.getByTestId("Tag2"));
    });
    expect(screen.queryByTestId("Tag2")).toEqual(null);
  });

  it("Back Space delete a Tag", async () => {
    const tags = screen.getByPlaceholderText("Enter a tag");

    await act(async () => {
      await populateTags(["Tag1", "Tag2"], user);
      hitKey(tags, "Backspace", "Backspace", 8, 8);
    });
    expect(screen.queryByTestId("Tag2")).toEqual(null);
  });
});
