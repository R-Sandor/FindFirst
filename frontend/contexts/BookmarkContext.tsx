import api from "@/api/Api";
import { NewBookmarkRequest } from "@/components/BookmarkGroup/bookmark/NewBookmarkCard";
import Bookmark from "@/types/Bookmarks/Bookmark";
import BookmarkAction from "@/types/Bookmarks/BookmarkAction";
import { Dispatch, createContext, useContext, useEffect, useReducer } from "react";

export const BookmarkContext = createContext<Bookmark[]>([]);
export const BookmarkDispatchContext = createContext<Dispatch<BookmarkAction>>(
  () => {}
);

export function BookmarkProvider({ children }: { children: React.ReactNode }) {
  const [bookmarks, dispatch] = useReducer(bookmarkReducer, initialBookmarks);

  useEffect(() => () => { 
    for(let i = 0; i < bookmarks.length; i++){
      bookmarks.pop()
    }
  }, [])
  return (
    <BookmarkContext.Provider value={bookmarks}>
      <BookmarkDispatchContext.Provider value={dispatch}>
        {children}
      </BookmarkDispatchContext.Provider>
    </BookmarkContext.Provider>
  );
}

function bookmarkReducer(bookmarkList: Bookmark[], action: BookmarkAction) {
  switch (action.type) {
    case "add": {
      console.info("should have added bookmark")
      if (action.bookmarks) {
        bookmarkList.push(...action.bookmarks) 
      }
      return  [...bookmarkList];
    }
    case "delete": {
      bookmarkList =  bookmarkList.filter((b, i) => b.id !== action.bookmarkId);
      if (action.bookmarkId) {
        const id = parseInt(action.bookmarkId.toString())
        api.deleteBookmarkById(id);
      }
      return bookmarkList
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
