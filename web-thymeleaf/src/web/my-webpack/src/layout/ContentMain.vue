<template>
  <section class="app-main">
        <router-view :key="key" />
  </section>
</template>

<script>
export default {
    name: 'ContentMain',
    computed: {
        key() {
            return this.$route.path
        }
    }

}
</script>

<style scoped>
.app-main {
    
    padding: 0;
    
}
</style>