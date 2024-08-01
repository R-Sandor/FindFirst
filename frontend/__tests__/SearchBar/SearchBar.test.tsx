import { beforeEach, describe, expect, it, vi } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import SearchBar from "@components/Navbar/SearchBar";

describe("SearchBar Component", () => {
  it("renders search input and button", () => {
    render(<SearchBar onSearch={vi.fn()} />);
    const input = screen.getByTestId("search-input");
    const button = screen.getByTestId("search-button");

    expect(input).toBeInTheDocument();
    expect(button).toBeInTheDocument();
  });

  it("calls onSearch with the correct query when the button is clicked", () => {
    const mockOnSearch = vi.fn();
    render(<SearchBar onSearch={mockOnSearch} />);

    const input = screen.getByTestId("search-input");
    const button = screen.getByTestId("search-button");

    fireEvent.change(input, { target: { value: "test query" } });
    fireEvent.click(button);

    expect(mockOnSearch).toHaveBeenCalledWith("test query");
  });
});
