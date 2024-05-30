import { beforeEach, describe, expect, it } from "vitest";
import { fireEvent, render, screen } from "@testing-library/react";
import NewBookmarkCard from "@components/BookmarkGroup/Bookmark/NewBookmarkCard";
// import { debug } from "vitest-preview";
import userEvent from "@testing-library/user-event";
import { act } from "react-dom/test-utils";
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

  it("All Required fields are given data", async () => {
    const submit = screen.getByText("Submit");
    const tags = screen.getByPlaceholderText("Enter a tag");
    await act(async () => {
      await user.type(
        screen.getByPlaceholderText(/discover/i),
        "foodnetwork.com",
      );
      await user.type(tags, "cooking");
    });
    fireEvent.keyDown(tags, {
      key: "Enter",
      code: "Enter",
      keyCode: "13",
      charCode: "13",
    });
    expect(submit).not.toBeDisabled();
    await user.click(submit);
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

  it("Reset option works", () => {});

  it("Too many tags!", () => {});

  it("Unsubmitted tag is added on submit", () => {});
});
