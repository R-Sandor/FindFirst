import Bookmark from "@type/Bookmarks/Bookmark";
import Tag, { TagReqPayload } from "@type/Bookmarks/Tag";

export const tagsData = [
  {
    id: 1,
    title: "Cooking",
    bookmarks: [
      {
        id: 3,
        title: "Chicken Parm",
        url: "https://www.foodnetwork.com/recipes/bobby-flay/chicken-parmigiana-recipe-1952359",
      },
    ],
  },
  {
    id: 2,
    title: "web_dev",
    bookmarks: [
      {
        id: 2,
        title: "Dark mode guide",
        url: "https://blog.logrocket.com/dark-mode-react-in-depth-guide/",
      },
    ],
  },
  {
    id: 3,
    title: "deserts",
    bookmarks: [
      {
        id: 1,
        title: "Best Cheesecake Recipe",
        url: "https://sugarspunrun.com/best-cheesecake-recipe/",
      },
    ],
  },
  {
    id: 4,
    title: "camping",
    bookmarks: [],
  },
  {
    id: 5,
    title: "spring docs",
    bookmarks: [],
  },
  {
    id: 6,
    title: "web docs",
    bookmarks: [],
  },
];

export const myTags: Tag[] = [
  {
    id: 1,
    title: "Cooking",
  },
  {
    id: 2,
    title: "web_dev",
  },
  {
    id: 3,
    title: "deserts",
  },
];
export const bkmkResp: Bookmark[] = [
  {
    id: 1,
    title: "Best Cheesecake Recipe",
    url: "https://sugarspunrun.com/best-cheesecake-recipe/",
    tags: [myTags[2]],
    screenshotUrl: "",
    scrapable: true,
  },
  {
    id: 2,
    title: "Dark mode guide",
    url: "https://blog.logrocket.com/dark-mode-react-in-depth-guide/",
    tags: [myTags[1]],
    screenshotUrl: "",
    scrapable: true,
  },
  {
    id: 3,
    title: "Chicken Parm",
    url: "https://www.foodnetwork.com/recipes/bobby-flay/chicken-parmigiana-recipe-1952359",
    tags: [myTags[0]],
    screenshotUrl: "",
    scrapable: true,
  },
];

export const defaultBookmark: Bookmark = {
  id: 1,
  title: "facebook.com",
  url: "facebook.com",
  tags: [
    {
      id: 1,
      title: "social",
    },
  ],
  screenshotUrl: "",
  scrapable: true,
};

export const firstTag: TagReqPayload[] = [
  {
    id: 2,
    title: "fb",
    bookmarks: [],
  },
];

export const secondTag: TagReqPayload[] = [
  {
    id: 3,
    title: "friends",
    bookmarks: [],
  },
];
