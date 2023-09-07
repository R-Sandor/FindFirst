"use client";
import api from "@/api/Api";
import Bookmark from "@/types/Bookmarks/Bookmark";
import BookmarkCard from "@components/bookmark/BookmarkCard";
import TagList from "@components/TagList";
import useAuth from "@components/UseAuth";
import TagWithCnt from "@/types/Bookmarks/TagWithCnt";
import {
  TagCntProvider,
} from "contexts/TagContext";
import { useEffect, useState } from "react";
import BookmarkGroup from "@/components/bookmark/BookmarkGroup";
import { useBookmarks } from "@/contexts/BookmarkContext";

export default function App() {
  const userAuth = useAuth();
  // const [bookmarks, setBookmarks] = useState<Bookmark[]>([]);
  const bookmarks = useBookmarks();
  const [loading, setLoading] = useState(true);

  // Grab the data.
  useEffect(() => {
    if (userAuth) {
      api.getAllBookmarks().then((results) => {
        for (let bkmk of results.data) {
          bookmarks.push(bkmk);
        }
        setLoading(false);
      });
    }
  }, [userAuth]);

  useEffect(() => {
    console.log(bookmarks)
  }, [bookmarks])

  /**
   * Ideally when the user visits the site they will actually have a cool landing page
   * rather than redirecting them immediately to sign in.
   * Meaning that the '/' will eventually be added to the public route and not authenticated will be the
   * the regular landing.
   */
  return userAuth ? (
    !loading ? (
      <TagCntProvider>
        <div className="row">
          <div className="col-md-4 col-lg-3">
            <TagList />
          </div>
          <div className="col-md-8 col-lg-9">
              <BookmarkGroup bookmarks={bookmarks}/>
          </div>
        </div>
      </TagCntProvider>
    ) : (
      <p>loading</p>
    )
  ) : (
    <div> Hello Welcome to BookmarkIt. </div>
  );
}
