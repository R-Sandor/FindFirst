import {
  Dispatch,
  createContext,
  useContext,
  useEffect,
  useReducer,
} from "react";
// import { Action } from "rxjs/internal/scheduler/Action";
import TagAction from "@/types/Bookmarks/TagAction";
import { TagWithCnt } from "@/types/Bookmarks/Tag";

export interface disapatchInterface {
  tagsWithCnt: Map<number, TagWithCnt>;
  action: TagAction;
}

export const TagsCntContext = createContext<Map<number, TagWithCnt>>(
  new Map<number, TagWithCnt>()
);
export const TagsCntDispatchContext = createContext<Dispatch<TagAction>>(
  () => {}
);

export function TagCntProvider({ children }: { children: React.ReactNode }) {
  useEffect(() => () => {
    tags.clear();
  });

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
  const tagId = action.tagId;
  const tagCnt: TagWithCnt | undefined = tagMap.get(action.tagId);
  // create a deep copy of the existing.
  const newTagMap = new Map(tagMap);
  switch (action.type) {
    case "add": {
      console.log("add Tag");
      if (tagCnt) {
        newTagMap.set(tagId, {
          tagTitle: tagCnt.tagTitle,
          count: tagCnt.count + 1,
        });
      } else {
        newTagMap.set(tagId, {
          tagTitle: action.tagTitle,
          count: 1,
        });
      }
      return newTagMap;
    }
    case "delete": {
      console.log("deleting");
      if (tagCnt && tagCnt.count > 1) {
        newTagMap.set(tagId, {
          tagTitle: tagCnt.tagTitle,
          count: tagCnt.count - 1,
        });
      } else {
        newTagMap.delete(action.tagId);
      }
      return newTagMap;
    }
    default: {
      throw Error("Unknown action: " + action.type);
    }
  }
}

export function useTags() {
  return useContext(TagsCntContext);
}

export function useTagsDispatch() {
  return useContext(TagsCntDispatchContext);
}

const initialTagCnts: Map<number, TagWithCnt> = new Map();
