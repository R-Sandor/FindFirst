<template>
    <div>
        <b-card img-src="https://picsum.photos/600/300/?image=25" img-alt="Image" img-top tag="article"
            style="max-width: 20rem" class="mb-2 card-hghlght mx-3 my-3" onClick='{console.log(this.bookmark)}'>
            <b-card-text>
                {{ bookmark.title }}
            </b-card-text>
        </b-card>
    </div>
</template>

<script>

export default {
    props: ["bookmark"],
    // data: function () {},
};
</script>
<style>
.card-hghlght:hover {
    opacity: 1;
    transform: scale(1.02);
}

.content:hover .card-hghlght:not(:hover) {
    opacity: 0.5;
    transform: scale(0.9);
}
</style>
