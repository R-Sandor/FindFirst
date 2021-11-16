<template>
    <div id="app">
        <h1>bookmarkit</h1>
        <bookmarks :bookmarks="bookmarks" />
    </div>
</template>

<script>
import Bookmarks from './components/Bookmarks';
import api from './Api';

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
    created() {
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
@import '../node_modules/bootstrap/dist/css/bootstrap.min.css'

</style>
