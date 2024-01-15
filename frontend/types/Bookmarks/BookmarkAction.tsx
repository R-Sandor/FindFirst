import Bookmark from "./Bookmark";

export default interface BookmarkAction {
    type: string;
    bookmarkId?: number;
    bookmarks?: Bookmark[];
}