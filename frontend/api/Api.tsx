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
  // Get all bookmarks.
  getAllBookmarks() {
    return this.execute("GET", "bookmarks", null, {});
  },
  // Delete all bookmarks.
  removeAllBookmarks() {
    return instance.delete("bookmarks");
  },
  // Gets a bookmark by ID.
  getBookmarkById(id: number) {
    return instance.get("bookmark", {
      params: { "id": id },
    });
  },
  // Adds a bookmark containing a list of tag Ids if any.
  addBookmark(bkmkReq: NewBookmarkRequest) {
    return instance.post("bookmark",  bkmkReq);
  },
  // Deletes a bookmark by ID.
  deleteBookmarkById(id: number) {
    return instance.delete("bookmark", {
      params: { "id": id },
    });
  },
  // Adds a list of new bookmarks.
  addBookmarks(bkmks: NewBookmarkRequest[]) {
    return instance.post("bookmark/addBookmarks",bkmks);
  },
  // Adds a tag to an existing bookmark by bookmark Id with just string title of tag.
  addBookmarkTag(bkmkId: number, title: string) { 
    return instance.post(`bookmark/${bkmkId}/tag?tag=${title}`)
  },
  // Adds an existing tag to an existing bookmark by ids.
  addTagById(bkmkId: number, tagId: number) { 
    return instance.post(`bookmark/${bkmkId}/tagId?tagId=${tagId}`)
  },
  // Deletes a tag from an existing bookmark using the bookmarkId and tag's title.
  deleteTagFromBookmark(bkmkId: number, title: string) { 
    return instance.delete(`bookmark/${bkmkId}/tag?tag=${title}`)
  },
  // Deletes a tag from an existing bookmark using the bookmarkId and tag ID.
  deleteTagById(bkmkId: number, tagId: number) { 
    return instance.delete(`bookmark/${bkmkId}/tag?tagId=${tagId}`)
  },
  // Gets all tags
  getAllTags() {
    return this.execute("GET", "tags", null, {});
  },
  // Create a tag no bookmark yet. 
  addAllTag(strTags: string []) {
    return this.execute("POST", "tags", strTags, {});
  },
  // Deletes all tags from bookmarks.
  deleteAllTags() { 
    return instance.delete("tags"); 
  },
  // Creates a new tag by title.
  addTag(title: string) { 
    return instance(`tag?tag=${title}`)
  },
  // Gets a tag by ID.
  getTagById(id: string) {
    return this.execute("GET", "tags/id/" + id, null, {});
  },
  // Returns all the tags that belong to a particular bookmark.
  getAllTagsBelongingToBkmk(bkmkId: number) { 
    return instance.get(`tag/bkmk?bookmarkId=${bkmkId}`)
  }
};

export default api;
