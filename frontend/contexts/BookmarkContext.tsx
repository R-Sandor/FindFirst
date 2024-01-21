import api from "@/api/Api";
import BookmarkAction from "@/types/Bookmarks/BookmarkAction";
import Bookmark from "@type/Bookmarks/Bookmark";
import {
  Dispatch,
  createContext,
  useContext,
  useEffect,
  useReducer,
  useState,
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
  () => {}
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

  useEffect(() => {
    console.log("fetching again")
    api.getAllBookmarks().then((resp) => {
      console.log(...resp.data);
      bookmarks.push(...(resp.data as Bookmark[]));
      setIsLoading(false);
    });
  }, []);

  // clean up hook
  useEffect(() => () => {
    console.log("clearing up bookmarks");
    for(let i = 0; i < bookmarks.length; i++){ 
      bookmarks.pop();
    }
  }, []);

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
      if (action.bookmarks) {
        bookmarkList.push(...action.bookmarks);
      }
      return [...bookmarkList];
    }
    case "delete": {
      bookmarkList = bookmarkList.filter((b, i) => b.id !== action.bookmarkId);
      if (action.bookmarkId) {
        const id = parseInt(action.bookmarkId.toString());
        api.deleteBookmarkById(id);
      }
      return bookmarkList;
    }
    default: {
      throw Error("Unknown action: " + action.type);
    }
  }
}
