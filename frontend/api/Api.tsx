import { NewBookmarkRequest } from "@/components/bookmark/NewBookmarkCard";
import axios from "axios";
const SERVER_URL = "http://localhost:9000/api";

const instance = axios.create({
  withCredentials: true,
  baseURL: SERVER_URL,
  timeout: 3000,
  transformResponse: [
    function (data: any) {
      return parseData(data);
    },
  ],
});

function parseData(data: string) {
  return data ? JSON.parse(data) : data;
}

const api = {
  async execute(
    method: string,
    resource: string,
    data: any | null | undefined,
    config: {} | undefined
  ) {
    return instance({
      method: method,
      url: resource,
      data,
    });
  },
  // (R)ead
  getAllBookmarks() {
    return this.execute("GET", "bookmarks", null, {});
  },
  removeAllBookmarks() {
    return instance.delete("bookmarks");
  },
  addBookmark(bkmkReq: NewBookmarkRequest) {
    return instance.post("bookmark",  bkmkReq);
  },
  deleteBookmarkById(id: number) {
    return instance.delete("bookmark", {
      params: { "id": id },
    });
  },
  addBookmarks(bkmks: NewBookmarkRequest[]) {
    return instance.post("bookmark/addBookmarks",bkmks);
  },
  getBookmarkById(id: number) {
    return instance.get("bookmark", {
      params: { "id": id },
    });
  },
  // Tags
  getAllTags() {
    return this.execute("GET", "tags", null, {});
  },
  addAllTag(strTags: string []) {
    let t = JSON.stringify(strTags);
    return this.execute("POST", "tags", strTags, {});
  },
  deleteAllTags() { 
    return instance.delete("tags"); 
  },
  getTagById(id: string) {
    return this.execute("GET", "tags/id/" + id, null, {});
  },
  addTag(tag: string) { 
    return instance.post("tag", tag)
  },
  getTagByBookmarkId(id: number) {
    return instance.get("tag/bkm", {
      params: { "bookmarkId": id }
    })
  },
};

export default api;
