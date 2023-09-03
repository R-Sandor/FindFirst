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

export default function App() {
  const userAuth = useAuth();
  const [bookmarks, setBookmarks] = useState<Bookmark[]>([]);
  const [loading, setLoading] = useState(true);

  // Grab the data.
  useEffect(() => {
    if (userAuth) {
      let bkmkList: Bookmark[] = [];
      let tagList: TagWithCnt[] = [];
      api.getAllBookmarks().then((results) => {
        for (let bkmk of results.data) {
          bkmkList.push(bkmk);
        }
        setBookmarks(bkmkList);
        setLoading(false);
      });
    }
  }, [userAuth]);

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
            <div className="row no-pad">
              <div className="col-6 col-md-12 col-lg-4">
                <BookmarkCard bookmark={bookmarks[0]} />
              </div>
              <div className="col-6 col-md-12 col-lg-4">
                <BookmarkCard bookmark={bookmarks[1]} />
              </div>
              <div className="col-6 col-md-12 col-lg-4">
                <BookmarkCard bookmark={bookmarks[2]} />
              </div>
            </div>
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
