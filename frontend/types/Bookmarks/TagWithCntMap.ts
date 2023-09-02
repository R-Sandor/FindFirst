import Tag from "./Tag";
import TagWithCnt from "./TagWithCnt";

// List of Tags that contain their count.
export default interface TagWithCntMap  {
    myMap: Map<number, TagWithCnt>
}