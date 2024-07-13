"use client";
import UseAuth from "@components/UseAuth";
import TagList from "@components/Tags/TagList";
import tagStyles from "@/styles/tag.module.scss";
import BookmarkCardsView from "@components/CardView/BookmarkCardsView";

export default function App() {
  const userAuth = UseAuth();

  /**
   * Ideally when the user visits the site they will actually have a cool landing page
   * rather than redirecting them immediately to sign in.
   * Meaning that the '/' will eventually be added to the public route and not authenticated will be the
   * the regular landing.
   */
  return userAuth ? (
    <div className="container-fluid">
      <div className="row">
        <div className={`col-md-4 col-lg-3 ${tagStyles.tagList}`}>
          <TagList />
        </div>
        <div className="col-md-8 col-lg-9">
          <BookmarkCardsView />
        </div>
      </div>
    </div>
  ) : (
    <div></div>
  );
}
