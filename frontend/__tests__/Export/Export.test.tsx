import { instance } from "@api/Api";
import Navbar from "@components/Navbar/Navbar";
import authService, { User } from "@services/auth.service";
import { render, screen } from "@testing-library/react";
import { userEvent } from "@testing-library/user-event";
import MockAdapter from "axios-mock-adapter";
import { beforeEach, describe, expect, it, vi } from "vitest";
const user = userEvent;
import fs from "node:fs";

beforeEach(async () => {
  const user: User = { username: "jsmith", refreshToken: "blahblajhdfh34234" };
  const axiosMock = new MockAdapter(instance);
  let blobData: Blob | undefined = undefined;
  fs.readFile("./README.md", (err, data) => {
    if (err) throw err;
    if (data) {
      blobData = new Blob([data], {});
    }
  });

  axiosMock.onGet().reply(() => {
    return [200, blobData];
  });
  vi.spyOn(authService, "getUser").mockImplementation(() => user);
  vi.spyOn(authService, "getAuthorized").mockImplementation(() => 1);
  render(<Navbar />);
});

describe("Test Export", () => {
  it("User can download bookmarks", () => {
    const exportButton = screen.getByTestId(/export-component/i);
    expect(exportButton).toBeInTheDocument();
    user.click(exportButton);
  });
});
