<template>
    <div id="app">
        <h1>bookmarkit</h1>
        <BookMarks />
    </div>
</template>

<script>
import BookMarks from './components/Bookmarks'
import axios from 'axios'

/*
  const SERVER_URL = 'http://localhost:9000';  
  
  const instance = axios.create({  
    baseURL: SERVER_URL,  
    timeout: 1000  
  });  
*/
// app Vue instance
const app = {
    name: 'app',
    components: {
        BookMarks,
    },
    // app initial state
    data: () => {
        return {
            response: [],
        }
    },
    beforeCreate() {
        axios
            .get(`http://localhost:9000/bookmarks`)
            .then((response) => {
                this.response = response.data
                this.$log.debug(response)
            })
            .catch((e) => {
                this.errors.push(e)
            })
        this.$log.debug('test', this.response)
    },
}

export default app
</script>

<style>
[v-cloak] {
    display: none;
}
</style>
