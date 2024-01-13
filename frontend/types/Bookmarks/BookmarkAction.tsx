import { NewBookmarkForm } from "@/components/BookmarkGroup/bookmark/NewBookmarkCard";
import Bookmark from "./Bookmark";

export default interface BookmarkAction {
    type: string;
    bookmarkId?: number;
    bookmark?: Bookmark;
}