<template>
    <div>
        <div class="card">
            <img class="mb-2 card-hghlght mx-3 my-3" src="https://picsum.photos/600/300/?image=25" img-alt="Image" img-top tag="article"
            style="max-width: 20rem" onClick='{console.log(this.bookmark)}'>
             <div class="card-body">
                <div class="card-text">
                {{ bookmark.title }}
                </div>
            </div> 
        </div>
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
