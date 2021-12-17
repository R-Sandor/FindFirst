<template>
    <!-- Bookmark pane. -->
    <b-row>
        <b-card-group flat v-for="(row, index) in arrangedData" :key="index">
            <Bookmark v-for="item in row" :key="item.id" :bookmark="item" />
        </b-card-group>
    </b-row>
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
    //emits: ['addItem'],
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
                console.log(arrangedData.length, item);
                arrangedData[arrangedData.length - 1].push(item);
            });
            console.log(arrangedData);
            return arrangedData;
        },
    },
};
</script>

<style></style>
