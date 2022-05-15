import axios from 'axios'  
  
const SERVER_URL = 'http://localhost:9000';  
  
const instance = axios.create({  
  baseURL: SERVER_URL,  
  timeout: 1000  
});  
  
export default {  
  
  async execute(method, resource, data, config) {  
    return instance({  
      method:method,  
      url: resource,  
      data,  
      ...config  
    })  
  },  
  
  // (C)reate  
  createNew(text, completed) {  
    return this.execute('POST', 'bookmarks', {title: text, completed: completed})  
  },  
  // (R)ead  
  getAll() {  
    return this.execute('GET','bookmarks', null, {  
      transformResponse: [function (data) {  
        console.log(JSON.parse(data));
        return data? JSON.parse(data) : data;  
      }]  
    })  
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
