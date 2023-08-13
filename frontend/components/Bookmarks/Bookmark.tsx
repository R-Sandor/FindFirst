import Tag from "./Tag";

export default interface Bookmark {
  id: string;
  title: string;
  url: URL;
  tags: Tag[];
}
