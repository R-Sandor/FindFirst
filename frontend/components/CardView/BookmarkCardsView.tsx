import { useSelectedTags } from "@/contexts/SelectedContext";
import BookmarkCard from "@components/Bookmark/BookmarkCard";
import NewBookmarkCard from "@components/Bookmark/NewBookmarkCard";
import { useBookmarks } from "@/contexts/BookmarkContext";
import Bookmark from "@type/Bookmarks/Bookmark";
import { useTags } from "@/contexts/TagContext";
import { TagWithCnt } from "@type/Bookmarks/Tag";
import cardView from "styles/cardView.module.scss";

function getTagId(map: Map<number, TagWithCnt>, tagTitle: string) {
  for (let [k, v] of map) {
    if (v.tagTitle === tagTitle) {
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

  function addIfNotInList(ids: number[]) {
    ids.forEach((bkmkId) => {
      const fnd = bookmarks.values.find((v) => v.id == bkmkId);
      if (fnd) filterMap.set(bkmkId, fnd);
    });
  }

  function filterBookmarks(bookmarks: Bookmark[]): Bookmark[] {
    if (selected.length == 0) {
      return bookmarks;
    } else {
      console.log("Selected", selected);
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

  return (
    <div>
      {!bookmarks.loading ? (
        <div className={`${cardView.content} max-h-inherit row pt-3`}>
          <div className="col-sm-12 col-md-12 col-lg-4">
            <NewBookmarkCard />
          </div>
          {filterBookmarks(bookmarks.values).map((b, i) => {
            return (
              <div key={i} className="col-sml-12 col-md-6 col-lg-4">
                <BookmarkCard bookmark={b} />
              </div>
            );
          })}
        </div>
      ) : (
        <p>loading</p>
      )}
    </div>
  );
}
