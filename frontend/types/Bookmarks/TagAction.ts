import Bookmark from "./Bookmark";

export default interface TagAction {
  type: string;
  id: number;
  title: string;
  bookmark: Bookmark;
}
