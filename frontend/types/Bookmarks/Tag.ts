import Bookmark from "./Bookmark";

export default interface Tag {
  id: number;
  title: string;
}

export interface TagReqPayload {
  id: number;
  title: string;
  bookmarks: Bookmark[];
}

// Tag with count associated bookmarks.
export interface TagWithCnt {
  title: string;
  associatedBkmks: Bookmark[];
  count: number;
}
