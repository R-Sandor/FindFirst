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
  const [bookmarks, dispatch] = useReducer(bookmarkReducer, initialBookmarks);

  return (
    <BookmarkContext.Provider value={bookmarks}>
      <BookmarkDispatchContext.Provider value={dispatch}>
        {children}
      </BookmarkDispatchContext.Provider>
    </BookmarkContext.Provider>
  );
}

function bookmarkReducer(bookmarkList: Bookmark[], action: BookmarkAction) {
  console.log("reducer");
  let newBkmkRequest: NewBookmarkRequest;
  switch (action.type) {
    case "add": {
      console.log("add event");
      bookmarkList.push(action.bookmark) 
      return [...bookmarkList];
    }
    case "set": {
      bookmarkList = [action.bookmark]
      return [...bookmarkList]
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
