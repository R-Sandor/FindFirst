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
  console.log(bookmarks);
  const userAuth = UseAuth();
  const [loading, setLoading] = useState(true);

  // Grab the data.
  useEffect(() => {
    if (userAuth) {
      api.getAllBookmarks().then((results) => {
          bookmarks.push(...results.data as Bookmark[])
      }).then(() => { 
          setLoading(false);
      });
    }
  }, [userAuth, bookmarks]);

  let bookmarkGroup: any = [];
  bookmarks.map((b, i) => {
    bookmarkGroup.push(
      <div key={i} className="col-6 col-md-12 col-lg-4">
        <BookmarkCard bookmark={b} />
      </div>
    );
  });

  return !loading ? (
    <div>
      {/* 
          This is where I want the search box to be for alpha. 
      */}
      {/* <div className="pt-3 pb-10 input-group">
        <input
          type="search"
          className="form-control rounded"
          placeholder="Search"
          aria-label="Search"
          aria-describedby="search-addon"
        />
        <button type="button" className="btn btn-outline-primary">
          search
        </button>
      </div> */}

      <div className="row pt-3">
        <div className="col-6 col-md-12 col-lg-4">
          <NewBookmarkCard />
        </div>
        {bookmarkGroup}
      </div>
    </div>
  ) : (
    <p>loading</p>
  );
}