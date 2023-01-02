<template>
    <!-- Bookmark pane. -->
    <div class="row">
        <div class="card-group" flat v-for="(row, index) in arrangedData" :key="index">
            <Bookmark v-for="item in row" :key="item.id" :bookmark="item" />
        </div>
    </div>
</template>

<script>
import Bookmark from './Bookmark';


export default {
    props: ['bookmarks'],
    data: function () {
        return {
            numCols: 4,
        };
    },
    components: {
        Bookmark
    },
    computed: {
        arrangedData() {
            const arrangedData = [];
            this.bookmarks.forEach((item, index) => {
                if (index % this.numCols === 0) {
                    arrangedData.push([]);
                }
                arrangedData[arrangedData.length - 1].push(item);
            });
            return arrangedData;
        },
    },
};
</script>

<style></style>
