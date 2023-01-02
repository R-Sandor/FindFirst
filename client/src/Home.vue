<template>
    <div class="row flex-xl-nowrap">
        <div class="col-12 col-md-3 bd-sidebar">
            <Sidebar />
        </div>
        <div class="col-12 col-md-9 pl-md-5 bd-content">
            <Bookmarks :bookmarks="bookmarks" />
        </div>
    </div>
</template>

<script>
import Bookmarks from './components/Bookmarks';
import Sidebar from './components/Sidebar';
import api from './Api';

export default {
    name: 'Home',
    components: {
        Bookmarks,
        Sidebar,
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
