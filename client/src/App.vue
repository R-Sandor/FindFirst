<template>
    <div id="app">
        <h1>bookmarkit</h1>
        <Bookmarks />
    </div>
</template>

<script>
import Bookmarks from './components/Bookmarks';
import api from './Api';

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
        Bookmarks,
    },
    // app initial state
    data: () => {
        return {
            response: [],
            bookmarks: [],
        };
    },
    beforeCreate() {
        api.getAll()
            .then((response) => {
                this.$log.debug('Data loaded: ', response.data);
                this.bookmarks = response.data;
            })
            .catch((error) => {
                this.$log.debug(error);
                this.error = 'Failed to load bookmarks';
            })
            .finally(() => (this.loading = false));
    },
};

export default app;
</script>

<style>
[v-cloak] {
    display: none;
}
</style>
