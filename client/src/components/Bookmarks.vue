<template>
    <b-row>
        <b-card-group flat v-for="(row, index) in arrangedData" :key="index">
            <b-card
                v-for="bookmark in row"
                :key="bookmark.id"
                title="bookmark.title"
                img-src="https://picsum.photos/600/300/?image=25"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 20rem"
                class="mb-2"
            >
                <b-card-text>
                    {{ bookmark.title }}
                </b-card-text>

                <b-button href="#" variant="primary">Go somewhere</b-button>
            </b-card>
        </b-card-group>
    </b-row>
</template>

<script>
export default {
    props: ['bookmarks'],
    data: function () {
        return {
            numCols: 3,
        };
    },
    //emits: ['addItem'],
    /*components: {
    Curr
  }*/
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

<style>

</style>
