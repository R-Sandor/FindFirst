"use client";
import { TagCntProvider } from "contexts/TagContext";
import { BookmarkProvider } from "@/contexts/BookmarkContext";
import UseAuth from "@components/UseAuth";
import TagList from "@components/tag/TagList";
import BookmarkGroup from "@components/BookmarkGroup/BookmarkGroup";
import tagStyles from '@/styles/tag.module.scss'
import bkmkStyles from '@/styles/bookmarkGrp.module.scss'

export default function App() {
  const userAuth = UseAuth();

  /**
   * Ideally when the user visits the site they will actually have a cool landing page
   * rather than redirecting them immediately to sign in.
   * Meaning that the '/' will eventually be added to the public route and not authenticated will be the
   * the regular landing.
   */
  return userAuth ? (
    <BookmarkProvider>
      <TagCntProvider>
        <div className="row">
          <div className={`col-md-4 col-lg-3 ${tagStyles.tagList}`}>
            <TagList />
          </div>
          <div className={`col-md-8 col-lg-9`}>
            <img className={` ${bkmkStyles.background_image}`} src="https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fimages.freecreatives.com%2Fwp-content%2Fuploads%2F2016%2F04%2FBlurred-Web-Backgrounds-HD-Wallpaper.jpg&f=1&nofb=1&ipt=66fe5c76a7bd517ddb5466a32781dfe393cb0503d8444748406b4fb916d8327a&ipo=images"></img>
            <BookmarkGroup />
          </div>
        </div>
      </TagCntProvider>
    </BookmarkProvider>
  ) : (
    <div> Hello Welcome to findfirst. </div>
  );
}
