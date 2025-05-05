import Bookmark from "./Bookmark";

export default interface PaginatedBookmarkRes {
  bookmarks: Bookmark[];
  totalPages: number;
  currentPage: number;
}