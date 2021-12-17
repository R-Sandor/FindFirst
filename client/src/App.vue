<template>
    <div id="app">
            <Navbar/>
        <div class="container-fluid">
            <div class="row flex-xl-nowrap">
                <div class="col-12 col-md-3 bd-sidebar">
                    <Sidebar />
                </div>
                <div
                    class="col-12 col-md-9 pl-md-5 bd-content"
                >
                    <Bookmarks :bookmarks="bookmarks" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Bookmarks from './components/Bookmarks';
import Navbar from './components/Navbar';
import Sidebar from './components/Sidebar';

import api from './Api';

// app Vue instance
const app = {
    name: 'app',
    components: {
        Bookmarks,
        Navbar,
        Sidebar,
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

<style></style>
