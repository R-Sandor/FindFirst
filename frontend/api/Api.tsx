import { NewBookmarkRequest } from "@/components/bookmark/NewBookmarkCard";
import axios from "axios";
const SERVER_URL = process.env.NEXT_PUBLIC_SERVER_URL + "/api";

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
  getBookmarkById(id: number) {
    return instance.get("bookmark", {
      params: { "id": id },
    });
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
  addBookmarkTag(bkmkId: number, title: string) { 
    return instance.post(`bookmark/${bkmkId}/tag?tag=${title}`)
  },
  addTagById(bkmkId: number, tagId: number) { 
    return instance.post(`bookmark/${bkmkId}/tagId?tagId=${tagId}`)
  },
  deleteTagFromBookmark(bkmkId: number, title: string) { 
    return instance.delete(`bookmark/${bkmkId}/tag?tag=${title}`)
  },
  deleteTagById(bkmkId: number, tagId: number) { 
    return instance.delete(`bookmark/${bkmkId}/tag?tagId=${tagId}`)
  },
  // Tags
  getAllTags() {
    return this.execute("GET", "tags", null, {});
  },
  addAllTag(strTags: string []) {
    return this.execute("POST", "tags", strTags, {});
  },
  deleteAllTags() { 
    return instance.delete("tags"); 
  },
  addTag(title: string) { 
    return instance(`tag?tag=${title}`)
  },
  getTagById(id: string) {
    return this.execute("GET", "tags/id/" + id, null, {});
  },
  getAllTagsBelongingToBkmk(bkmkId: number) { 
    return instance.get(`tag/bkmk?bookmarkId=${bkmkId}`)
  }
};

export default api;
