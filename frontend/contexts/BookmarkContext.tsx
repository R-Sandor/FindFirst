"use client";

import api from "@/api/Api";
import BookmarkAction from "@/types/Bookmarks/BookmarkAction";
import PaginatedBookmarkRes from "@/types/Bookmarks/PaginatedBookmarkRes";
import UseAuth from "@components/UseAuth";
import Bookmark from "@type/Bookmarks/Bookmark";
import {
  Dispatch,
  createContext,
  useContext,
  useEffect,
  useReducer,
  useState,
  useMemo,
  useCallback,
} from "react";

interface ProviderProps {
  fetchedBookmarks: Bookmark[];
  loading: boolean;
  currentPage: number;
  totalPages: number;
  hasMore: boolean;
  loadNextPage: () => void;
}
export const BookmarkContext = createContext<ProviderProps>({
  fetchedBookmarks: [],
  loading: true,
  currentPage: 1,
  totalPages: 1,
  hasMore: false,
  loadNextPage: () => {},
});

export const BookmarkDispatchContext = createContext<Dispatch<BookmarkAction>>(
  () => {},
);

export function useBookmarks() {
  return useContext(BookmarkContext);
}

export function useBookmarkDispatch() {
  return useContext(BookmarkDispatchContext);
}

export function BookmarkProvider({
  children,
}: {
  readonly children: React.ReactNode;
}) {
  const [bookmarks, dispatch] = useReducer(bookmarkReducer, []);
  const [isLoading, setIsLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const userAuth = UseAuth();
  const PAGE_SIZE = 10;

  const loadBookmarks = useCallback((page: number = 1) => {
    if (!userAuth) return;
    
    setIsLoading(true);
    api.getPaginatedBookmarks(page, PAGE_SIZE).then((resp) => {
      const data = resp.data as PaginatedBookmarkRes;
      
      if (page === 1) {
        // First page, replace all bookmarks
        dispatch({ 
          type: "replace", 
          bookmarks: data.bookmarks,
          currentPage: data.currentPage,
          totalPages: data.totalPages
        });
      } else {
        // Additional pages, append bookmarks
        dispatch({ 
          type: "append", 
          bookmarks: data.bookmarks,
          currentPage: data.currentPage,
          totalPages: data.totalPages
        });
      }
      
      setCurrentPage(data.currentPage);
      setTotalPages(data.totalPages);
      setIsLoading(false);
    }).catch(error => {
      console.error("Error loading bookmarks:", error);
      setIsLoading(false);
    });
  }, [userAuth]);

  const loadNextPage = useCallback(() => {
    if (currentPage < totalPages && !isLoading) {
      loadBookmarks(currentPage + 1);
    }
  }, [currentPage, totalPages, isLoading, loadBookmarks]);

  useEffect(() => {
    if (userAuth && bookmarks.length === 0) {
      loadBookmarks(1);
    }
  }, [userAuth, bookmarks.length, loadBookmarks]);

  useEffect(() => {
    return () => {
      dispatch({
        type: "reset",
        bookmarks: [],
      });
    };
  }, [userAuth]);

  const value = useMemo(
    () => ({ 
      fetchedBookmarks: bookmarks, 
      loading: isLoading,
      currentPage,
      totalPages,
      hasMore: currentPage < totalPages,
      loadNextPage
    }),
    [bookmarks, isLoading, currentPage, totalPages, loadNextPage],
  );

  return (
    <BookmarkContext.Provider value={value}>
      <BookmarkDispatchContext.Provider value={dispatch}>
        {children}
      </BookmarkDispatchContext.Provider>
    </BookmarkContext.Provider>
  );
}

function bookmarkReducer(bookmarkList: Bookmark[], action: BookmarkAction) {
  switch (action.type) {
    case "add": {
      return [...bookmarkList, ...action.bookmarks];
    }
    case "append": {
      return [...bookmarkList, ...action.bookmarks];
    }
    case "replace": {
      return [...action.bookmarks];
    }
    case "delete": {
      if (action.bookmarkId) {
        const id = parseInt(action.bookmarkId.toString());
        api.deleteBookmarkById(id);
      }
      return [...bookmarkList.filter((b) => b.id !== action.bookmarkId)];
    }
    case "reset": {
      return [];
    }
    case "search": {
      return action.bookmarks;
    }
    default: {
      throw Error("Unknown action: " + action.type);
    }
  }
}
