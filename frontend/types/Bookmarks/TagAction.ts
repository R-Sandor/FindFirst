import Bookmark from "./Bookmark";

export default interface TagAction {
  type: string;
  tagId: number;
  tagTitle: string;
  bookmark: Bookmark
}
