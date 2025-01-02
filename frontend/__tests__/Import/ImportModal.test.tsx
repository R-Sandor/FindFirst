import { beforeEach, describe, expect, it, vi } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import authService, { User } from "@services/auth.service";
import { bkmkResp } from "../data/SampleData";
import ImportModal from "@components/Import/ImportModal";

beforeEach(async () => {
  const user: User = { username: "jsmith", refreshToken: "blahblajhdfh34234" };
  // Mock GET request to /users when param `searchText` is 'John'
  // arguments for reply are (status, data, headers)
  vi.spyOn(authService, "getUser").mockImplementation(() => user);
  vi.spyOn(authService, "getAuthorized").mockImplementation(() => 1);
});

describe("Reads from Stream", async () => {
  vi.stubGlobal("fetch", async () => {
    return {
      body: new CustomReadable(bkmkResp).getStream(),
    };
  });

  const fs = require("fs");
  const readFile = async (path: any, callback: any) => {
    await fs.readFile(path, async (err: any, data: any) => {
      if (err) {
        console.error(err);
      } else {
        callback(data);
      }
    });
  };

  it("should have 3 bookmarks", async () => {
    // Not really reading the contents of the file. We could
    // but for the purpose of this test a file needed to be passed
    // to force an import.
    await readFile("./README.md", (data: File) => {
      const blob = new Blob([data], {});
      render(
        <div>
          <ImportModal file={blob} show={true} />
        </div>,
      );
    });
    const bt = await screen.findByText(bkmkResp[0].title, undefined, {
      timeout: 5000,
    });
    expect(bt).toBeInTheDocument();
    expect(screen.getAllByTestId(/imported-bkmk-/i).length).toEqual(3);
  });
});

// Create a custom readable stream
class CustomReadable {
  private data: any[];
  private index: number;

  constructor(data: any[]) {
    this.data = data;
    this.index = 0;
  }

  getStream() {
    return new ReadableStream({
      start: () => {
        this.index = 0;
      },
      pull: (controller) => {
        if (this.index < this.data.length) {
          controller.enqueue(
            new TextEncoder().encode(JSON.stringify(this.data[this.index])),
          );
          this.index++;
        } else {
          controller.close();
        }
      },
    });
  }
}
