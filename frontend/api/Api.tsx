import { NewBookmarkRequest } from "@/components/bookmark/NewBookmarkCard";
import axios from "axios";
const SERVER_URL = "http://localhost:9000/api";

const instance = axios.create({
  withCredentials: true,
  baseURL: SERVER_URL,
  timeout: 3000,
  transformResponse: [
    function (data) {
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

  // (C)reate
  addNewBookmark(newBkmkReq: NewBookmarkRequest) {
    return this.execute("POST", "bookmark/add", newBkmkReq, {});
  },
  // (R)ead
  getAllBookmarks() {
    return this.execute("GET", "bookmarks", null, {});
  },
  bookmarkAddTagByTitle(bookmarkId: string | number, tagTitle: string) {
    return this.execute("POST", "bookmark/" + bookmarkId + "/addTag", {
      tag_title: tagTitle,
    }, {});
  },
  bookmarkRemoveTagById(bookmarkId: string | number, tagId: number) {
    console.log("removing bookmark-tag", bookmarkId, tagId)
    return instance.delete("bookmark/" + bookmarkId + "/tagId", {
      params: { id: tagId },
    });
  },
  bookmarkRemoveTagByTitle(bookmarkId: string, title: any) {
    return instance.delete("bookmark/" + bookmarkId + "/tagTitle", {
      params: { title: title },
    });
  },
  // (D)elete
  removeBookmarkById(bookmarkId: number) {
    return instance.delete("bookmark/" + bookmarkId);
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
