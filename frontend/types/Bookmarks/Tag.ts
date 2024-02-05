import Bookmark from "./Bookmark";

export default interface Tag {
  id: number;
  tag_title: string;
}

export interface TagReqPayload {
  id: number;
  tag_title: string;
  bookmarks: Bookmark[];
}

// Tag with count associated bookmarks.
export interface TagWithCnt {
  tagTitle: string;
  associatedBkmks: Bookmark[]
  count: number;
}
