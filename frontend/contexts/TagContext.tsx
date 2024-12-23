import { Dispatch, createContext, useContext, useReducer } from "react";
import TagAction from "@/types/Bookmarks/TagAction";
import { TagWithCnt } from "@/types/Bookmarks/Tag";
import Bookmark from "@type/Bookmarks/Bookmark";

export interface disapatchInterface {
  tagsWithCnt: Map<number, TagWithCnt>;
  action: TagAction;
}

export const TagsCntContext = createContext<Map<number, TagWithCnt>>(
  new Map<number, TagWithCnt>(),
);
export const TagsCntDispatchContext = createContext<Dispatch<TagAction>>(
  () => {},
);

export function TagCntProvider({ children }: { children: React.ReactNode }) {
  const [tags, dispatch] = useReducer(tagCntReducer, initialTagCnts);

  return (
    <TagsCntContext.Provider value={tags}>
      <TagsCntDispatchContext.Provider value={dispatch}>
        {children}
      </TagsCntDispatchContext.Provider>
    </TagsCntContext.Provider>
  );
}

function tagCntReducer(tagMap: Map<number, TagWithCnt>, action: TagAction) {
  const tagId = action.id;
  const tagCnt: TagWithCnt | undefined = tagMap.get(action.id);
  // create a deep copy of the existing.
  const newTagMap = new Map(tagMap);

  const bkmk = action.bookmark;

  switch (action.type) {
    case "add": {
      if (tagCnt) {
        addBkmkToTag(tagCnt, action.bookmark);
        newTagMap.set(tagId, {
          title: tagCnt.title,
          count: tagCnt.count + 1,
          associatedBkmks: tagCnt.associatedBkmks,
        });
      } else {
        newTagMap.set(tagId, {
          title: action.title,
          count: 1,
          associatedBkmks: [bkmk],
        });
      }
      return newTagMap;
    }
    case "delete": {
      if (tagCnt && tagCnt.count > 1) {
        newTagMap.set(tagId, {
          title: tagCnt.title,
          count: tagCnt.count - 1,
          associatedBkmks: remBkmkFrmTag(tagCnt, action.bookmark),
        });
      } else {
        newTagMap.delete(action.id);
      }
      return newTagMap;
    }
    default: {
      throw Error("Unknown action: " + action.type);
    }
  }
}

function addBkmkToTag(tagCnt: TagWithCnt, bkmk: Bookmark) {
  let fnd = false;
  if (tagCnt) {
    const associatedBkmks = tagCnt.associatedBkmks;
    for (let i = 0; i < associatedBkmks.length; i++) {
      if (associatedBkmks[i].title == bkmk.title) {
        fnd = true;
        break;
      }
    }
  }
  if (!fnd) {
    tagCnt?.associatedBkmks.push({ ...bkmk });
  }
}

function remBkmkFrmTag(tagCnt: TagWithCnt, bkmk: Bookmark) {
  return tagCnt.associatedBkmks.filter((b) => b.title !== bkmk.title);
}

export function useTags() {
  return useContext(TagsCntContext);
}

export function useTagsDispatch() {
  return useContext(TagsCntDispatchContext);
}

const initialTagCnts: Map<number, TagWithCnt> = new Map();
