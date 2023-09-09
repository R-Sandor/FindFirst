import api from "@/api/Api";
import { NewBookmarkRequest } from "@/components/bookmark/NewBookmarkCard";
import Bookmark from "@/types/Bookmarks/Bookmark";
import BookmarkAction from "@/types/Bookmarks/BookmarkAction";
import Tag from "@/types/Bookmarks/Tag";
import { Dispatch, createContext, useContext, useReducer } from "react";

export const BookmarkContext = createContext<Bookmark[]>([]);
export const BookmarkDispatchContext = createContext<Dispatch<BookmarkAction>>(
  () => {}
);

export function BookmarkProvider({ children }: { children: React.ReactNode }) {
  const [tags, dispatch] = useReducer(bookmarkReducer, initialBookmarks);

  return (
    <BookmarkContext.Provider value={tags}>
      <BookmarkDispatchContext.Provider value={dispatch}>
        {children}
      </BookmarkDispatchContext.Provider>
    </BookmarkContext.Provider>
  );
}

function bookmarkReducer(bookmarkList: Bookmark[], action: BookmarkAction) {
  console.log("reducer");
  let newBkmk = action.bookmark;
  let newBkmkRequest: NewBookmarkRequest;
  switch (action.type) {
    case "add": {
      console.log("add event");
      newBkmkRequest = {
        title: newBkmk.title,
        url: newBkmk.url,
        tagIds: [],
      };
      console.log(newBkmk)
      console.log(newBkmk.tags)
      let tagTitles: string[] = newBkmk.tags.map((t, i) => { return t.tag_title});
      api.addAllTag(tagTitles).then((response) => {
        let respTags: Tag[] = response.data;
        respTags.forEach((rt) => {
          console.log(rt)
          newBkmkRequest.tagIds.push(rt.id);
        });

      api.addNewBookmark(newBkmkRequest).then((response) => {
        console.log(response.data);
        newBkmk.id = response.data.id;
        newBkmk.tags = response.data.tags;
        bookmarkList.push(newBkmk);
      });
      });
      return bookmarkList;
    }
    case "delete": {
      bookmarkList.filter((b, i) => b.id !== action.bookmarkId);
    }
    default: {
      throw Error("Unknown action: " + action.type);
    }
  }
}

// function

export function useBookmarks() {
  return useContext(BookmarkContext);
}

export function useBookmarkDispatch() {
  return useContext(BookmarkDispatchContext);
}

const initialBookmarks: Bookmark[] = [];
