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

  

  return (
    <div>
      { !bookmarks.loading? (
      <div className="row pt-3">
        <div className="col-6 col-md-12 col-lg-4">
          <NewBookmarkCard />
        </div>
        {bookmarks.values.map((b, i) => {
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
