"use client";

import api from "@/api/Api";
import BookmarkAction from "@/types/Bookmarks/BookmarkAction";
import UseAuth from "@components/UseAuth";
import Bookmark from "@type/Bookmarks/Bookmark";
import {
  Dispatch,
  createContext,
  useContext,
  useEffect,
  useReducer,
  useState,
  useRef,
} from "react";

interface ProviderProps {
  values: Bookmark[];
  loading: boolean;
}
export const BookmarkContext = createContext<ProviderProps>({
  values: [],
  loading: true,
});
export const BookmarkDispatchContext = createContext<Dispatch<BookmarkAction>>(
  () => { },
);

export function useBookmarks() {
  return useContext(BookmarkContext);
}

export function useBookmarkDispatch() {
  return useContext(BookmarkDispatchContext);
}

export function BookmarkProvider({ children }: { children: React.ReactNode }) {
  const [bookmarks, dispatch] = useReducer(bookmarkReducer, []);
  const [isLoading, setIsLoading] = useState(true);
  const hasFetched = useRef(false);
  const userAuth = UseAuth();

  useEffect(() => {
    if (userAuth && bookmarks.length == 0 && !hasFetched.current) {
      console.log("Bookmarks being fetched");
      hasFetched.current = true;
      api.getAllBookmarks().then((resp) => {
        dispatch({ type: "add", bookmarks: resp.data as Bookmark[] });
        setIsLoading(false);
      });
    }
  }, [bookmarks.length, userAuth]);


  useEffect(() => {
    return () => {
      hasFetched.current = false
      dispatch({
        type: "reset",
        bookmarks: []
      });
    }
  }, [userAuth]);


  return (
    <BookmarkContext.Provider value={{ values: bookmarks, loading: isLoading }}>
      <BookmarkDispatchContext.Provider value={dispatch}>
        {children}
      </BookmarkDispatchContext.Provider>
    </BookmarkContext.Provider>
  );
}

function bookmarkReducer(bookmarkList: Bookmark[], action: BookmarkAction) {
  switch (action.type) {
    case "add": {
      console.log("adding");
      return [...bookmarkList, ...action.bookmarks];
    }
    case "delete": {
      console.log("DELETE");
      if (action.bookmarkId) {
        const id = parseInt(action.bookmarkId.toString());
        api.deleteBookmarkById(id);
      }
      return [...bookmarkList.filter((b) => b.id !== action.bookmarkId)];
    }
    case "reset": {
      return [];
    }
    default: {
      throw Error("Unknown action: " + action.type);
    }
  }
}
