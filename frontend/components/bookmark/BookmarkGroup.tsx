import Bookmark from "@/types/Bookmarks/Bookmark";
import BookmarkCard from "./BookmarkCard";
import NewBookmarkCard from "./NewBookmarkCard";
import { useBookmarks } from "@/contexts/BookmarkContext";
import { useEffect } from "react";
import UseAuth from "@/components/UseAuth";
import api from "@/api/Api";
import BookmarkAction from "@/types/Bookmarks/BookmarkAction";

// Bookmark group composed of Bookmarks.
export default function BookmarkGroup() {
  const bookmarks = useBookmarks();
  const userAuth = UseAuth();

  // Grab the data.
  useEffect(() => {
    let b: Bookmark[] = [];
    if (userAuth) {
      console.log(bookmarks);
      api.getAllBookmarks().then((results) => {
        for (let bkmk of results.data) {
          console.log(bkmk);
          let bkmkAction: BookmarkAction = {
            type: "add",
            bookmark: bkmk,
          };
          bookmarks.push(bkmk)
        }
      });
    }
  }, [userAuth,  bookmarks]);

  let bookmarkGroup: any = [];
  bookmarks.map((b, i) => {
    bookmarkGroup.push(
      <div key={i} className="col-6 col-md-12 col-lg-4">
        <BookmarkCard bookmark={b} />
      </div>
    );
  });

  return (
    <div className="row no-pad">
      <div className="col-6 col-md-12 col-lg-4">
        <NewBookmarkCard />
      </div>
      {bookmarkGroup}
    </div>
  );
}
