import { beforeEach, describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import NewBookmarkCard from "@components/BookmarkGroup/Bookmark/NewBookmarkCard";
import { debug } from "vitest-preview";
import userEvent from "@testing-library/user-event";
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

describe("All Fields Work", () => {
  beforeEach(() => {
    render(
      <div data-bs-theme="dark" className="row pt-3">
        <div className="col-6 col-sm-12 col-md-12 col-lg-4">
          <NewBookmarkCard />
        </div>
      </div>,
    );
  });

  it("No fields have data, should be disabled", () => {
    const submit = screen.getByText("Submit");
    expect(submit).toBeDisabled();
  });

  it("All Required fields are given data", async () => {
    await user.type(screen.getByPlaceholderText(/discover/i), "facebook.com");
    const submit = screen.getByText("Submit");
    expect(submit).not.toBeDisabled();
  });

  it("Is a valid domain", async () => {
    await user.type(
      screen.getByPlaceholderText(/discover/i),
      "facebook.whatever",
    );
    const submit = screen.getByText("Submit");
    expect(submit).toBeDisabled();
  });

  it("Reset option works", () => {});

  it("Unsubmitted tag is added on submit", () => {});
});
