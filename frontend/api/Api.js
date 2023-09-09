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

function parseData(data) {
  return data ? JSON.parse(data) : data;
}

const api = {
  async execute(method, resource, data, config) {
    return instance({
      method: method,
      url: resource,
      data,
    });
  },

  // (C)reate
  addNewBookmark(newBkmkReq) {
    return this.execute("POST", "bookmark/add", newBkmkReq)
  },
  // (R)ead
  getAllBookmarks() {
    return this.execute("GET", "bookmarks", null, {
  
    });
  },
  bookmarkAddTagByTitle(bookmarkId, tagTitle) {
    return this.execute(
      "POST",
      "bookmark/" + bookmarkId + "/addTag",
      { tag_title: tagTitle },
    );
  },
  bookmarkRemoveTagById(bookmarkId, tagId) {
    return instance.delete("bookmark/" + bookmarkId + "/tagId", {
      params: { id: tagId }
    })
  },
  bookmarkRemoveTagByTitle(bookmarkId, title) {
    return instance.delete("bookmark/" + bookmarkId + "/tagTitle", {
      params: { title: title },
    });
  },
  getAllTags() {
    return this.execute("GET", "tagscnt", null, {
     
    });
  },
  addAllTag(strTags){
    let t = JSON.stringify(strTags);
    console.log(t)
    return this.execute("POST", "tags/addTags", strTags)
  },
  getTagById(id) {
    return this.execute("GET", "tags/id/" + id);
  },
  // (U)pdate
  updateForId(id, text, completed) {
    return this.execute("PUT", "bookmarks/" + id, {
      title: text,
      completed: completed,
    });
  },

  // (D)elete
  removeBookmarkById(id) {
    return this.execute("DELETE", "bookmarks/" + id);
  },
};

export default api;
