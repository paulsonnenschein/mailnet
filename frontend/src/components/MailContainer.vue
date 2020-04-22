<template>
  <div id="grid-main">
    <div id="grid-list">
      <MailList :mails="mails" @mailSelected="selectMail" @refresh="loadMail" />
    </div>
    <div id="grid-viewer">
      <MailViewer v-if="selectedMail" :mail="selectedMail" />
    </div>
  </div>
</template>

<script>
import MailList from "./MailList.vue";
import MailViewer from "./MailViewer.vue";

export default {
  name: "MailContainer.vue",
  components: {
    MailList,
    MailViewer
  },
  data() {
    return {
      mails: [],
      selectedMail: null
    };
  },
  async created() {
    await this.loadMail();
  },
  methods: {
    async loadMail() {
      this.mails = await fetch("/messages").then(r => r.json());
    },
    selectMail(mail) {
      this.selectedMail = mail;
    }
  }
};
</script>

<style>
#grid-main {
  display: grid;
  height: 100vh;
  width: 100vw;
  grid-template: "list viewer" / 25%;
}
#grid-list {
  grid-area: list;
}
#grid-viewer {
  grid-area: viewer;
}
</style>
