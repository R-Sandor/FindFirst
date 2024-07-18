import { beforeAll, beforeEach, describe, expect, it, test, vi } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import { act } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import authService, { User } from "@services/auth.service";
import Navbar from "@components/Navbar/Navbar";
import { bkmkResp } from "../data/SampleData";
import ImportModal from "@components/Import/ImportModal";
import { debug } from "vitest-preview";

beforeEach(async () => {
  const user: User = { username: "jsmith", refreshToken: "blahblajhdfh34234" };
  // Mock GET request to /users when param `searchText` is 'John'
  // arguments for reply are (status, data, headers)
  vi.spyOn(authService, "getUser").mockImplementation(() => user);
  vi.spyOn(authService, "getAuthorized").mockImplementation(() => 1);
  await act(async () => {
    render(
      <div>
        <Navbar />
      </div>,
    );
  });
});

describe("Reads from Stream", async () => {
  vi.stubGlobal("fetch", async (url: string, options: any) => {
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

  it("should have 4 bookmarks", async () => {
    console.log("Testing loading bookmarks");
    let modal;
    await readFile("./README.md", (data: File) => {
      console.log(data);
      const blob = new Blob([data], {});
      console.log(blob);
      modal = <ImportModal file={blob} show={true} />;
      console.log("modal", modal);
      render(<div>{modal}</div>);
    });
    debug();
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
      start: (controller) => {
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
