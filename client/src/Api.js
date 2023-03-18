import axios from 'axios'  
const SERVER_URL = 'api';  
  
const instance = axios.create({  
  withCredentials:true,
  baseURL: SERVER_URL,  
  timeout: 1000  
});  

  // const headers = [];
  // const config  = { headers}
 

export default {  
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
  getAll() {  
    console.log("getall")
    return this.execute('GET','bookmarks', null, { 
      transformResponse: [function (data) {  
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
