<template>
    <a @click="link">
        <slot></slot>
    </a>
</template>

<script>
export default {
    props: {
        href: {
            type: String,
            required: true
        }
    },
    methods: {
        link(event) {
            event.preventDefault()
            this.$router.push({name: this.href})
            console.log("route " + this.href)
        }
    }
    
}
</script>