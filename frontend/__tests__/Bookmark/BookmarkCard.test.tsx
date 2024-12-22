import { beforeEach, describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
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
    render(
      <div data-bs-theme="dark" className="row pt-3">
        <div className="col-6 col-sm-12 col-md-12 col-lg-4">
          <BookmarkCard bookmark={defaultBookmark} />
        </div>
      </div>,
    );
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
    await user.click(screen.getByTestId("bk-id-1-deleteBtn"));
    await user.click(screen.getByText(/yes/i));
  });
});

describe("Adding and deleting Tags", () => {
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
                  title: "social",
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

  it("Adding tags", async () => {
    const axiosMock = new MockAdapter(instance);
    axiosMock.onPost().replyOnce(() => {
      return [200, JSON.stringify(firstTag)];
    });
    axiosMock.onPost().replyOnce(() => {
      return [200, JSON.stringify(secondTag)];
    });
    await populateTags(["fb", "friends"], user);
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
    await user.click(screen.getByText(/social/i));
    expect(screen.queryByText(/social/i)).toBeNull();
  });

  it("Deleting tag on backspace", async () => {
    const axiosMock = new MockAdapter(instance);
    axiosMock.onDelete().replyOnce(() => {
      return [
        200,
        JSON.stringify({
          id: 1,
          title: "socail",
        }),
      ];
    });
    const tags = screen.getByPlaceholderText("Enter a tag");
    hitKey(tags, "backspace", "backspace", 8, 8);
    expect(screen.queryByText(/social/i)).toBeNull();
  });

  it("Edit bookmark", async () => {
    const axiosMock = new MockAdapter(instance);
    axiosMock.onPatch().reply(() => {
      return [200, JSON.stringify({})];
    });
    const editBkmkBtn = screen.getByTestId("1-edit-btn");
    await user.click(editBkmkBtn);
    const editBkmkTitle = screen.getByTestId("title-facebook.com-edit-input");
    const editBkmkUrl = screen.getByTestId("url-facebook.com-edit-input");
    await user.type(editBkmkTitle, "-new");
    await user.type(editBkmkUrl, "/home");
    const toggle = screen.getByTestId(/1-scrapable-edit/i);
    await user.click(toggle);
    await user.type(editBkmkTitle, "{enter}");
    await user.click(editBkmkBtn);
    expect(screen.getByDisplayValue("facebook.com-new")).toBeInTheDocument();
    expect(screen.getByDisplayValue("facebook.com/home")).toBeInTheDocument();
    expect(toggle).not.toBeChecked();
  });
});
