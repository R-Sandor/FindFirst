"use client";
import api from "@/api/Api";
import authService, { AuthStatus } from "@/services/auth.service";
import Tag from "@/types/Bookmarks/Tag";
import Bookmark from "@/types/Bookmarks/Bookmark";
import TagList from "@components/TagList";
import { useEffect, useState } from "react";

export default function App() {
  const [authorized, setAuthorized] = useState<AuthStatus>();
  const [tag, setTags] = useState<Tag[]>([]);
  const [bookmarks, setBookmarks] = useState<Bookmark[]>([]);

  useEffect(() => {
    setAuthorized(authService.getAuthorized());
  }, []);

  // Grab the data.
  useEffect(() => {
    if (authorized) {
      let tagList: Tag[] = [];
      let bkmkList: Bookmark[] = [];
      api.getAllTags().then((response) => {
        for (let tag of response.data) {
          tagList.push(tag);
        }
      });
      setTags(tagList);
      console.log(tagList);

      api.getAllBookmarks().then((response) => {
        for (let bkmk of response.data) {
          bkmkList.push(bkmk);
        }
      });
      console.log(bkmkList);
    }
  }, [authorized]);

  /**
   * Ideally when the user visits the site they will actually have a cool landing page
   * rather than redirecting them immediately to sign in.
   * Meaning that the '/' will eventually be added to the public route and not authenticated will be the
   * the regular landing.
   */
  return authorized ? (
    <div>
      <div className="w-1/4">
        <TagList tagsCounted={[]} />
      </div>
    </div>
  ) : (
    <div> Hello Welcome to BookmarkIt. </div>
  );
}
