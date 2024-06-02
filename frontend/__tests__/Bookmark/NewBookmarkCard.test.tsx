import { beforeEach, describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import NewBookmarkCard from "@components/Bookmark/NewBookmarkCard";
import userEvent from "@testing-library/user-event";
import { act } from "react-dom/test-utils";
import MockAdapter from "axios-mock-adapter";
import { TagReqPayload } from "@type/Bookmarks/Tag";
import { instance } from "@api/Api";
import Bookmark from "@type/Bookmarks/Bookmark";
import { hitEnter, hitKey } from "../utilities/fireEvents";
import { populateTags } from "../utilities/BookmarkUtils/BookmarkUtil";
const user = userEvent.setup();

describe("New Bookmark Card Renders", () => {
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

  it("Card renders", () => {
    expect(screen.getByText("Add Bookmark")).toBeDefined();
    expect(screen.getByPlaceholderText(/discover/i)).toBeDefined();
    expect(screen.getByPlaceholderText("Enter a tag")).toBeDefined();
    expect(screen.getByText("Reset")).toBeDefined();
    expect(screen.getByText("Submit")).toBeDefined();
  });
});

describe("All Fields Work", () => {
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
    expect(url).toHaveValue("foodnetwork.com");

    const axiosMock = new MockAdapter(instance);
    const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL;
    const tagsAPI = SERVER_URL + "/api/tags";
    const bookmarkAPI = SERVER_URL + "/api/bookmark";

    const expectedResult: TagReqPayload[] = [
      {
        id: 1,
        tag_title: "cooking",
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
          tag_title: "cooking",
        },
      ],
    };

    axiosMock.onPost(tagsAPI, ["cooking"]).reply((config) => {
      return [200, JSON.stringify(expectedResult)];
    });

    axiosMock
      .onPost(bookmarkAPI, {
        title: "foodnetwork.com",
        url: "foodnetwork.com",
        tagIds: [1],
      })
      .reply((config) => {
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

  it("Is a valid domain", async () => {
    const submit = screen.getByText("Submit");
    await act(async () => {
      await user.type(
        screen.getByPlaceholderText(/discover/i),
        "facebook.whatever",
      );
    });
    expect(submit).toBeDisabled();
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
    });
    await user.click(reset);
    expect(url).toHaveValue("");
    expect(tags).toHaveValue("");
    expect(submit).toBeDisabled();
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
