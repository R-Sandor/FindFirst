/**
 * Bookmark representation from the NewBookmarkCard card form..
 */
export interface NewBookmarkForm {
  id?: string;
  title: string;
  url: string;
  tagTitles: string[];
}

/**
 * The actual request made to server.
 */
export interface NewBookmarkRequest {
  title: string;
  url: string;
  tagIds: number[];
  scrapable: boolean
}

/**
 * NewCard form.
 */
export const newcard: NewBookmarkForm = {
  title: "",
  url: "",
  tagTitles: [],
};
