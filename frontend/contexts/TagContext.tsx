import { Dispatch, createContext, useContext, useReducer} from "react";
import TagWithCnt from '@type/Bookmarks/TagWithCnt';
// import { Action } from "rxjs/internal/scheduler/Action";
import Action from "@/types/Bookmarks/Action";

export interface disapatchInterface { 
 tagsWithCnt: Map<number, TagWithCnt>
 action: Action
}

export const TagsCntContext = createContext<Map<number, TagWithCnt>>(new Map<number, TagWithCnt>());
export const TagsCntDispatchContext = createContext<Dispatch<Action>>(() => {
});


export function TagCntProvider({ children }: {
  children: React.ReactNode
}) {
  const [tags, dispatch] = useReducer(
    tagCntReducer,
    initialTagCnts 
  );

  return (
    <TagsCntContext.Provider value={tags}>
      <TagsCntDispatchContext.Provider value={dispatch}>
        {children}
      </TagsCntDispatchContext.Provider>
    </TagsCntContext.Provider>
  );
}

function tagCntReducer(tagsWithCnt: Map<number, TagWithCnt>, action: Action) {
  switch (action.type) {
    case "add": {
      console.log("add");
      let tagCnt: TagWithCnt | undefined = tagsWithCnt.get(action.tagId);
      if (tagCnt) {
        console.log("THERE IS A COUNT")
        console.log(tagCnt)
        tagCnt.count = tagCnt.count + 1;
        tagsWithCnt.set(action.tagId, tagCnt)
      } else {
        let tId = action.tagId ? action.tagId : -1;
        tagsWithCnt.set(action.tagId, {
          // TODO: use ids!
          tag: { id: tId, tag_title: action.tagTitle },
          count: 1,
        });
      }
      return new Map(tagsWithCnt);
    }
    case "delete": {
      let tagCnt: TagWithCnt | undefined = tagsWithCnt.get(action.tagId);
      console.log("deleting");
      if (tagCnt && tagCnt.count > 1) {
        tagCnt.count--;
      } else {
        tagsWithCnt.delete(action.tagId);
      }
      return new Map(tagsWithCnt);
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