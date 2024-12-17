import About from "@/app/about/page";
import { render, screen } from "@testing-library/react";
import { beforeEach, describe, expect, it } from "vitest";

describe("Describe page content check", () => {
  beforeEach(() => {
    render(<About />);
  });

  it("Test Page Loads", () => {
    expect(screen.getByText(/about us/i)).toBeInTheDocument();
  });
});
