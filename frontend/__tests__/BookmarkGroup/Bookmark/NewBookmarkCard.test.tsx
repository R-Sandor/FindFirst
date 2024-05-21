import { describe, expect, it, test, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import NewBookmarkCard from "@components/BookmarkGroup/Bookmark/NewBookmarkCard";
import authService, { User } from "@services/auth.service";
import { instance } from "@api/Api";
import Tag from "@type/Bookmarks/Tag";
import { debug } from "vitest-preview";

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
    debug();
    expect(screen.getByText("Add Bookmark")).toBeDefined();
  });
});

describe("All Fields Work", () => {
  it("No fields have data, should be disabled", () => {});

  it("All fields are given data", () => {});

  it("Reset option works", () => {});

  it("Unsubmitted tag is added on submit", () => {});
});
