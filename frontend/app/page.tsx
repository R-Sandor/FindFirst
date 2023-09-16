"use client";
import "./main.css"
import TagList from "@components/TagList";
import { TagCntProvider } from "contexts/TagContext";
import BookmarkGroup from "@/components/bookmark/BookmarkGroup";
import { BookmarkProvider } from "@/contexts/BookmarkContext";
import UseAuth from "@components/UseAuth";

export default function App() {
  const userAuth = UseAuth();

  /**
   * Ideally when the user visits the site they will actually have a cool landing page
   * rather than redirecting them immediately to sign in.
   * Meaning that the '/' will eventually be added to the public route and not authenticated will be the
   * the regular landing.
   */
  // return userAuth ? (
  //   <BookmarkProvider>
  //     <TagCntProvider>
  //       <div className="row">
  //         <div className="col-md-4 col-lg-3">
  //           <TagList />
  //         </div>
  //         <div className="col-md-8 col-lg-9">
  //           <BookmarkGroup />
  //         </div>
  //       </div>
  //     </TagCntProvider>
  //   </BookmarkProvider>
  // ) : (
  //   <div> Hello Welcome to BookmarkIt. </div>
  // );
  return (
    <div className="row">
      <div className="row">
        <div className="pt-5 pb-5 half-width-form-control">
          <div className="input-group">
            <input
              type="search"
              className="form-control rounded"
              placeholder="Describe your figure!"
              aria-label="Search"
              aria-describedby="search-addon"
            />
            <button type="button" className="btn btn-outline-primary">
              search
            </button>
          </div>
        </div>
      </div>
      <div className="col-3">
        <div className="form-check">
          <input className="form-check-input" type="checkbox" value="" id="flexCheckDefault" />
          <label className="form-check-label" htmlFor="flexCheckDefault">
            Algorithms
          </label>
        </div>
        <div className="form-check">
          <input className="form-check-input" type="checkbox" value="" id="flexCheckChecked" />
          <label className="form-check-label" htmlFor="flexCheckChecked">
            Architecture Diagram
          </label>
        </div>
        <div className="form-check">
          <input className="form-check-input" type="checkbox" value="" id="flexCheckChecked"  />
          <label className="form-check-label" htmlFor="flexCheckChecked">
            Bar Charts
          </label>
        </div>
      </div>
      <div className="col-9">
        Filter here
      </div>
    </div>
  )
}
