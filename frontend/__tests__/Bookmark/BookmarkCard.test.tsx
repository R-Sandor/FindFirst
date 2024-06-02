import { beforeEach, describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import { act } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { debug } from "vitest-preview";
import BookmarkCard from "@components/Bookmark/BookmarkCard";
import { populateTags } from "../utilities/BookmarkUtils/BookmarkUtil";
const user = userEvent.setup();

describe("Bookmark functions", () => {
  beforeEach(() => {
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
    await populateTags(["fb", "friends"], user);
    debug();
  });
  it("Deleting tags", () => {});
});
