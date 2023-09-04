import Bookmark from "@/types/Bookmarks/Bookmark";
import BookmarkCard from "./BookmarkCard";
import NewBookmarkCard from "./NewBookmarkCard";

// Bookmark group composed of Bookmarks.
export default function BookmarkGroup({
  bookmarks,
}: {
  bookmarks: Bookmark[];
}) {

  let bookmarkGroup: any = [];
  bookmarks.map((b, i) => {
    bookmarkGroup.push(
      <div key={i} className="col-6 col-md-12 col-lg-4">
        <BookmarkCard bookmark={b} />
      </div>
    )
  })

  return (
    <div className="row no-pad">
     <div className="col-6 col-md-12 col-lg-4">
        <NewBookmarkCard  />
      </div>
        { bookmarkGroup } 
    </div>
  );
}
