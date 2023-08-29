import axios from 'axios'  
const SERVER_URL = "http://localhost:9000/api";  
  
const instance = axios.create({  
  withCredentials:true,
  baseURL: SERVER_URL,  
  timeout: 1000  
});  

  // const headers = [];
  // const config  = { headers}

function parseData(data) {
  return data? JSON.parse(data) : data;  
}

const api = {  
  async execute(method, resource, data) {  
    return instance({  
      method:method,  
      url: resource,  
      data  
    })  
  },  
  
  // (C)reate  
  createNew(text, completed) {  
    return this.execute('POST', 'bookmarks', {title: text, completed: completed})  
  },  
  // (R)ead  
  getAllBookmarks() {  
    return this.execute('GET','bookmarks', null, { 
      transformResponse: [function (data) {  
        return parseData(data)
      }]  
    })  
  },  
  bookmarkAddTag (bookmarkId, tagTitle) {
    return this.execute('POST', 'bookmark/' + bookmarkId + '/addTag', { tag_title: tagTitle})
  },
  getAllTags() {  
    return this.execute('GET','tagscnt', null, { 
      transformResponse: [function (data) {  
        return parseData(data)
      }]  
    })  
  },
  getTagsForId(id) { 
    return this.execute('GET', 'tags/id/' + id);
  },
  // (U)pdate  
  updateForId(id, text, completed) {  
    return this.execute('PUT', 'bookmarks/' + id, { title: text, completed: completed })  
  },  
  
  // (D)elete  
  removeForId(id) {  
    return this.execute('DELETE', 'bookmarks/'+id)  
  }  
}

export default api;