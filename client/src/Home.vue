<template>
    <div>
        <div class="col-12 pl-md-12 bd-content">
            <Bookmarks :bookmarks="bookmarks" />
        </div>
    </div>
</template>

<script>
// import Bookmarks from './components/ListView/ListGroupBookmarks.vue';
import api from './Api';

export default {
    name: 'Home',
    components: {
        // Bookmarks,
    },
    data() {
        return {
            response: [],
            bookmarks: [],
        };
    },
    created() {
        api.getAll()
            .then((response) => {
                console.debug('Data loaded: ', response.data);
                this.bookmarks = response.data;
            })
            .catch((error) => {
                console.error(error);
                this.error = 'Failed to load bookmarks';
            })
            .finally(() => (this.loading = false));
    },
    mounted() {},
};
</script>
