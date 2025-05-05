import { useSelectedTags } from "@/contexts/SelectedContext";
import BookmarkCard from "@components/Bookmark/BookmarkCard";
import NewBookmarkCard from "@components/Bookmark/NewBookmarkCard";
import { useBookmarks } from "@/contexts/BookmarkContext";
import Bookmark from "@type/Bookmarks/Bookmark";
import { useTags } from "@/contexts/TagContext";
import { TagWithCnt } from "@type/Bookmarks/Tag";
import cardView from "styles/cardView.module.scss";
import { useEffect, useRef, useCallback, useContext } from "react";

function getTagId(map: Map<number, TagWithCnt>, tagTitle: string) {
  for (let [k, v] of map) {
    if (v.title === tagTitle) {
      return k;
    }
  }
  return -1;
}

// Bookmark group composed of Bookmarks.
export default function BookmarkCardsView() {
  const bookmarks = useBookmarks();
  const { selected } = useSelectedTags();
  const tags = useTags();
  const filterMap = new Map<number, Bookmark>();
  const observerTarget = useRef<HTMLDivElement>(null);

  const { fetchedBookmarks, loading, hasMore, loadNextPage } = bookmarks;

  function addIfNotInList(ids: number[]) {
    ids.forEach((bkmkId) => {
      const fnd = fetchedBookmarks.find((v) => v.id == bkmkId);
      if (fnd) filterMap.set(bkmkId, fnd);
    });
  }

  function filterBookmarks(bookmarks: Bookmark[]): Bookmark[] {
    if (selected.length == 0) {
      return bookmarks;
    } else {
      filterMap.clear();
      selected.forEach((selectedTag) => {
        // get tagId of each selected
        const key = getTagId(tags, selectedTag);
        if (key > 0) {
          const selectedBkmks = tags.get(key)?.associatedBkmks.map((v) => v.id);
          if (selectedBkmks) {
            addIfNotInList(selectedBkmks);
          }
        }
      });
      return [...filterMap.values()];
    }
  }

  // Set up intersection observer for infinite scrolling
  const handleObserver = useCallback((entries: IntersectionObserverEntry[]) => {
    const [target] = entries;
    if (target.isIntersecting && hasMore && !loading) {
      loadNextPage();
    }
  }, [hasMore, loading, loadNextPage]);

  useEffect(() => {
    // Check if IntersectionObserver is available (not available in test environment)
    if (typeof IntersectionObserver !== 'undefined') {
      const observer = new IntersectionObserver(handleObserver, {
        root: null,
        rootMargin: '20px',
        threshold: 0.1
      });
      
      if (observerTarget.current) {
        observer.observe(observerTarget.current);
      }
      
      return () => {
        if (observerTarget.current) {
          observer.unobserve(observerTarget.current);
        }
      };
    } else {
      // For test environment without IntersectionObserver
      const checkIfShouldLoadMore = () => {
        if (hasMore && !loading) {
          loadNextPage();
        }
      };
      
      // In tests, we can manually trigger this function
      if (process.env.NODE_ENV === 'test') {
        checkIfShouldLoadMore();
      }
    }
  }, [handleObserver, hasMore, loading, loadNextPage]);

  const filteredBookmarks = filterBookmarks(fetchedBookmarks);

  return (
    <div>
      <div className={`${cardView.content} row`}>
        <div className="col-sm-12 col-md-12 col-lg-4">
          <NewBookmarkCard />
        </div>
        {filteredBookmarks.map((b) => {
          return (
            <div key={b.id} className="col-sml-12 col-md-6 col-lg-4">
              <BookmarkCard bookmark={b} />
            </div>
          );
        })}
      </div>
      
      {/* Loading indicator and observer target */}
      <div ref={observerTarget} className="loading-observer" style={{ height: '20px', margin: '20px 0' }}>
        {loading && <p className="text-center">Loading more bookmarks...</p>}
      </div>
    </div>
  );
}
