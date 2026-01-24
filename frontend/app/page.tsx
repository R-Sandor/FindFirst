"use client";
import UseAuth from "@components/UseAuth";
import TagList from "@components/Tags/TagList";
import tagStyles from "@/styles/tag.module.scss";
import navStyles from "@/styles/navbar.module.scss";
import sideStyles from "@/styles/sidemenu.module.scss";
import BookmarkCardsView from "@components/CardView/BookmarkCardsView";
import { ScreenSizeProvider } from "@/contexts/ScreenSizeContext";
import NewBookmarkCard from "@components/Bookmark/NewBookmarkCard";
import Image from "next/image";
export default function App() {
  const userAuth = UseAuth();

  /**
   * Ideally when the user visits the site they will actually have a cool landing page
   * rather than redirecting them immediately to sign in.
   * Meaning that the '/' will eventually be added to the public route and not authenticated will be the
   * the regular landing.
   */
  return userAuth ? (
    <div className={`container-fluid`}>
      <div className={`row ${navStyles.fullHeightRow}`}>
        <ScreenSizeProvider>
          <div
            className={`col-md-4 col-lg-2 col-xl-1 fixed-top ${tagStyles.sideMenu}`}
          >
            <div className={``}>
              <div>
                <Image
                  src="/basic-f-v2-dark-mode-v2-fav.png"
                  width="65"
                  height="48"
                  className={sideStyles.logoPicture}
                  alt="FindFirst Logo"
                />
                <h4 className={`d-inline-block ${sideStyles.logo}`}>
                  findFirst
                </h4>
              </div>
            </div>
            <NewBookmarkCard />
            <TagList />
          </div>
        </ScreenSizeProvider>
        <div className={` col-md-8 col-lg-10  ${navStyles.scrollableColumn}`}>
          <BookmarkCardsView />
        </div>
      </div>
    </div>
  ) : null;
}
