import { beforeEach, describe, expect, it } from "vitest";
import { act, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import BookmarkCard from "@components/Bookmark/BookmarkCard";
import { populateTags } from "../utilities/BookmarkUtils/BookmarkUtil";
import { instance } from "@api/Api";
import MockAdapter from "axios-mock-adapter";
import { hitKey } from "../utilities/fireEvents";
import { defaultBookmark, firstTag, secondTag } from "../data/SampleData";
const user = userEvent.setup();

describe("Bookmark functions", () => {
  beforeEach(() => {
    act(() => {
      render(
        <div data-bs-theme="dark" className="row pt-3">
          <div className="col-6 col-sm-12 col-md-12 col-lg-4">
            <BookmarkCard bookmark={defaultBookmark} />
          </div>
        </div>,
      );
    });
  });

  it("Card Functionality", () => {
    const fb = screen.getAllByText(/facebook.com/i);
    expect(fb.length).toEqual(2);
    expect(screen.getByText(/social/i)).toBeInTheDocument();
  });

  it("Deleting Bookmark", async () => {
    const axiosMock = new MockAdapter(instance);
    axiosMock.onDelete().reply(() => {
      return [200, JSON.stringify("Deleting Bookmark 1")];
    });
    await act(async () => {
      await user.click(screen.getByTestId("bk-id-1-deleteBtn"));
    });
    await act(async () => {
      await user.click(screen.getByText(/yes/i));
    });
  });
});

describe("Adding and deleting Tags", () => {
  beforeEach(() => {
    act(() => {
      render(
        <div data-bs-theme="dark" className="row pt-3">
          <div className="col-6 col-sm-12 col-md-12 col-lg-4">
            <BookmarkCard
              bookmark={{
                id: 1,
                title: "facebook.com",
                url: "facebook.com",
                tags: [
                  {
                    id: 1,
                    title: "soial",
                  },
                ],
                scrapable: true,
                screenshotUrl: "",
              }}
            />
          </div>
        </div>,
      );
    });
  });

  it("Adding tags", async () => {
    const axiosMock = new MockAdapter(instance);
    axiosMock.onPost().replyOnce(() => {
      return [200, JSON.stringify(firstTag)];
    });
    axiosMock.onPost().replyOnce(() => {
      return [200, JSON.stringify(secondTag)];
    });
    await act(async () => {
      await populateTags(["fb", "friends"], user);
    });
    expect(screen.getByText(/fb/i)).toBeInTheDocument();
    expect(screen.getByText(/friends/i)).toBeInTheDocument();
  });

  it("Deleting tags on click", async () => {
    const axiosMock = new MockAdapter(instance);
    axiosMock.onDelete().replyOnce(() => {
      return [
        200,
        JSON.stringify({
          id: 1,
          title: "social",
        }),
      ];
    });
    await act(async () => {
      await user.click(screen.getByText(/socail/i));
    });
    expect(screen.queryByText(/socail/i)).toBeNull();
  });

  it("Deleting tag on backspace", async () => {
    const axiosMock = new MockAdapter(instance);
    axiosMock.onDelete().replyOnce(() => {
      return [
        200,
        JSON.stringify({
          id: 1,
          tag_title: "socail",
        }),
      ];
    });
    const tags = screen.getByPlaceholderText("Enter a tag");
    await act(async () => {
      hitKey(tags, "backspace", "backspace", 8, 8);
    });
    expect(screen.queryByText(/socail/i)).toBeNull();
  });
});
