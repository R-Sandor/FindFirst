import Bookmark from "./Bookmark";

export default interface Tag {
  id: number;
  tag_title: string;
  bookmarks: Bookmark[]
}
