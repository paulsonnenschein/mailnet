<template>
  <div id="app">
    <MailContainer />
  </div>
</template>

<script>
import MailContainer from "./components/MailContainer.vue";

export default {
  name: "App",
  components: {
    MailContainer
  }
};
</script>

<style>
body {
  margin: 0;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
