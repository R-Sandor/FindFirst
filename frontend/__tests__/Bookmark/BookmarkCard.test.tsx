import { beforeEach, describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import { act } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { debug } from "vitest-preview";
import BookmarkCard from "@components/Bookmark/BookmarkCard";
import { populateTags } from "../utilities/BookmarkUtils/BookmarkUtil";
import { instance } from "@api/Api";
import MockAdapter from "axios-mock-adapter";
import { TagReqPayload } from "@type/Bookmarks/Tag";
const user = userEvent.setup();

describe("Bookmark functions", () => {
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
                    tag_title: "socail",
                  },
                ],
              }}
            />
          </div>
        </div>,
      );
    });
  });

  it("Card renders", () => {
    const fb = screen.getAllByText(/facebook.com/i);
    expect(fb.length).toEqual(2);
    expect(screen.getByText(/socail/i)).toBeInTheDocument();
  });

  it("Deleting Bookmark", () => {});
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
                    tag_title: "socail",
                  },
                ],
              }}
            />
          </div>
        </div>,
      );
    });
  });

  it("Adding tags", async () => {
    const axiosMock = new MockAdapter(instance);
    const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL;
    const tagsAPI = SERVER_URL + "/api/";
    // bookmark/${bkmkId}/tag?tag=${title}
    const bookmarkAPI = SERVER_URL + "/api/bookmark";
    const firstTag: TagReqPayload[] = [
      {
        id: 2,
        tag_title: "fb",
        bookmarks: [],
      },
    ];
    const secondTag: TagReqPayload[] = [
      {
        id: 3,
        tag_title: "friends",
        bookmarks: [],
      },
    ];

    axiosMock.onPost().replyOnce(() => {
      return [200, JSON.stringify(firstTag)];
    });
    axiosMock.onPost().replyOnce(() => {
      return [200, JSON.stringify(secondTag)];
    });
    await act(async () => {
      await populateTags(["fb", "friends"], user);
    });
    debug();
  });
  it("Deleting tags", () => {});
});
