import Tag from "./Tag";

export default interface Bookmark {
  id: number;
  title: string;
  url: string;
  tags: Tag[];
}
