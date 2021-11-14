<template>
    <transition-group name="bookmarkslist" appear>
        <div v-for="bookmark in bookmarks"
            :key="bookmark.id"
        >
                <h1>{{ bookmark.title }}</h1>
                <h2>{{ bookmark.url }} </h2>
        </div>
    </transition-group>
</template>

<script>
export default {
  props: ['bookmarks']
  //emits: ['addItem'],
  /*components: {
    Curr
  }*/
}
</script>

<style></style>
