import { beforeEach, describe, expect, test, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import Page from "../app/page";
import authService, { User } from "@services/auth.service";
import { instance } from "@api/Api";
import { bkmkResp, tagsData } from "./data/SampleData";
import userEvent from "@testing-library/user-event";
import Navbar from "@components/Navbar/Navbar";
import { hitEnter } from "./utilities/fireEvents";
import { TagReqPayload } from "@type/Bookmarks/Tag";
import Bookmark from "@type/Bookmarks/Bookmark";
import { Providers } from "@/app/providers";
const userEvnt = userEvent.setup();

const data = JSON.stringify(bkmkResp, null, 2);

let mock: any;
beforeEach(async () => {
  const user: User = { username: "jsmith", refreshToken: "blahblajhdfh34234" };
  let MockAdapter = require("axios-mock-adapter");
  mock = new MockAdapter(instance);
  // Mock GET request to /users when param `searchText` is 'John'
  // arguments for reply are (status, data, headers)
  mock.onGet("/bookmarks").reply(200, data);
  mock.onGet("/tags").reply(200, JSON.stringify(tagsData));
  vi.spyOn(authService, "getUser").mockImplementation(() => user);
  vi.spyOn(authService, "getAuthorized").mockImplementation(() => 1);
  render(
    <div>
      <Providers>
        <Navbar />
        <Page />
      </Providers>
    </div>,
  );
});

describe("User is authenticated and bookmark/tag data is present.", () => {
  test("should be bookmarks available", async () => {
    const bkmkCard = await screen.findByText(/Best Cheesecake/i, undefined, {
      timeout: 1000,
    });
    expect(bkmkCard).toBeInTheDocument();
  });

  test("User clicks a tag in list", async () => {
    const bkmkCard = await screen.findByText(
      /Best Cheesecake Recipe/i,
      undefined,
      { timeout: 1000 },
    );
    expect(bkmkCard).toBeInTheDocument();
    await userEvnt.click(screen.getByTestId("deserts-list-item"));
    let allbkmks = screen.getAllByTestId(/bookmark-/i);
    expect(allbkmks.length).toBe(1);
    await userEvnt.click(screen.getByTestId("deserts-list-item"));
    allbkmks = screen.getAllByTestId(/bookmark-/i);
    expect(allbkmks.length).toBe(3);
  });

  test("User adds a tag", async () => {
    mock.onPost().replyOnce(() => {
      return [
        200,
        JSON.stringify({
          id: 1,
          title: "Cooking",
          bookmarks: [],
        }),
      ];
    });
    mock.onPost().replyOnce(() => {
      return [
        200,
        JSON.stringify({
          id: 10,
          title: "new",
          bookmarks: [],
        }),
      ];
    });
    const inputForCheeseCakeCard = await screen.findByTestId(
      "Best Cheesecake Recipe-input",
      undefined,
      { timeout: 1000 },
    );
    await userEvnt.type(inputForCheeseCakeCard, "Cooking");
    hitEnter(inputForCheeseCakeCard);

    await userEvnt.type(inputForCheeseCakeCard, "new");
    hitEnter(inputForCheeseCakeCard);
    expect(screen.getByTestId("Cooking-list-item-cnt")).toContainHTML(
      "<div data-testid='Cooking-list-item-cnt'>2</div>",
    );
    expect(await screen.findByTestId("new-list-item-cnt")).toContainHTML(
      "<div data-testid='new-list-item-cnt'>1</div>",
    );
  });

  test("User deletes a tag", async () => {
    mock.onDelete().replyOnce(() => {
      return [
        200,
        JSON.stringify({
          id: 2,
          title: "web_dev",
        }),
      ];
    });

    mock.onDelete().replyOnce(() => {
      return [
        200,
        JSON.stringify({
          id: 1,
          title: "Cooking",
        }),
      ];
    });

    mock.onPost().replyOnce(() => {
      return [
        200,
        JSON.stringify({
          id: 1,
          title: "Cooking",
          bookmarks: [],
        }),
      ];
    });

    const inputForCheeseCakeCard = await screen.findByTestId(
      "Best Cheesecake Recipe-input",
      undefined,
      { timeout: 1000 },
    );
    await userEvnt.type(inputForCheeseCakeCard, "Cooking");
    hitEnter(inputForCheeseCakeCard);

    await userEvnt.click(screen.getByTestId("web_dev-tag-2-bk"));

    await userEvnt.click(screen.getByTestId("Cooking-tag-1-bk"));
    expect(screen.getByTestId("Cooking-list-item-cnt")).toContainHTML(
      "<div data-testid='Cooking-list-item-cnt'>1</div>",
    );
  });
});

describe("Clicks around the page.", () => {
  test("Change theme.", async () => {
    let toggle = screen.getByTestId("light-dark");
    await userEvnt.click(toggle);
    expect(localStorage.getItem("theme")).toBe("light");
    toggle = screen.getByTestId("light-dark");
    await userEvnt.click(toggle);
    expect(localStorage.getItem("theme")).toBe("dark");
  });

  test("Logout click", async () => {
    await userEvnt.click(screen.getByText(/logout/i));
    const allbkmks = screen.queryAllByTestId(/bookmark-/i);
    expect(allbkmks).toEqual([]);
  });
});

describe("Bookmark Operation.", () => {
  test("Delete Bookmark: Testing bookmark context.", async () => {
    mock.onDelete().reply(() => {
      return [200, JSON.stringify("Deleting Bookmark 1")];
    });
    const bkId = 1;
    await userEvnt.click(await screen.findByTestId(`bk-id-${bkId}-deleteBtn`));
    await userEvnt.click(await screen.findByText(/yes/i));
    const allbkmks = screen.getAllByTestId(/bookmark-/i);
    expect(allbkmks.length).toBe(2);
  });

  test("Add Bookmark: Testing bookmark context.", async () => {
    const submit = await screen.findByText("Submit");
    const tags = screen.getByTestId("new-bk-tag-input");
    const url = screen.getByPlaceholderText(/discover/i);
    await userEvnt.type(url, "foodnetwork.com");
    await userEvnt.type(tags, "cooking");
    hitEnter(tags);
    expect(submit).not.toBeDisabled();

    // fields should be populated
    expect(url).toHaveValue("https://foodnetwork.com");

    const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL;
    const tagsAPI = SERVER_URL + "/api/tags";
    const bookmarkAPI = SERVER_URL + "/api/bookmark";

    const expectedResult: TagReqPayload[] = [
      {
        id: 1,
        title: "Cooking",
        bookmarks: [],
      },
    ];

    const expectedBookmark: Bookmark = {
      id: 10,
      title: "foodnetwork.com",
      url: "https://foodnetwork.com",
      screenshotUrl: "foodnetwork.com",
      tags: [
        {
          id: 1,
          title: "cooking",
        },
      ],
      scrapable: true,
    };

    mock.onPost(tagsAPI, ["cooking"]).reply(() => {
      return [200, JSON.stringify(expectedResult)];
    });

    mock
      .onPost(bookmarkAPI, {
        title: "https://foodnetwork.com",
        url: "https://foodnetwork.com",
        tagIds: [1],
        scrapable: true,
      })
      .reply(() => {
        return [200, JSON.stringify(expectedBookmark)];
      });

    await userEvnt.click(submit);
    const allbkmks = screen.getAllByTestId(/bookmark-/i);
    expect(allbkmks.length).toBe(4);
  });
});
