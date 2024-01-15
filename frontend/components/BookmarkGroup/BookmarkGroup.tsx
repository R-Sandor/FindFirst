import Bookmark from "@/types/Bookmarks/Bookmark";
import BookmarkCard from "./bookmark/BookmarkCard";
import NewBookmarkCard from "./bookmark/NewBookmarkCard";
import { useBookmarks } from "@/contexts/BookmarkContext";
import { useEffect, useState } from "react";
import UseAuth from "@/components/UseAuth";
import api from "@/api/Api";

// Bookmark group composed of Bookmarks.
export default function BookmarkGroup() {
  const bookmarks = useBookmarks();
  const userAuth = UseAuth();
  const [loading, setLoading] = useState(true);

  // Grab the data.
  useEffect(() => {
    if (userAuth) {
      console.log("getting all bookmarks")
      api.getAllBookmarks().then((resp) => {
        console.log(...resp.data);
        bookmarks.push(...(resp.data as Bookmark[]))
        setLoading(false);
      });
    }
  }, [userAuth]);

  return (
    <div>
      { !loading? (
      <div className="row pt-3">
        <div className="col-6 col-md-12 col-lg-4">
          <NewBookmarkCard />
        </div>
        {bookmarks.map((b, i) => {
          return (
            <div key={i} className="col-6 col-md-12 col-lg-4">
              <BookmarkCard bookmark={b} />
            </div>
          );
        })} 
      </div>)
      :(<p>loading</p>)
    }
    </div>
  );
}
