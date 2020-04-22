<template>
  <div>
    <h1>Mail List</h1>
    <button @click="$emit('refresh')">Refresh</button>
    <ul>
      <li v-for="mail in mails" :key="mail.id">
        {{ mail.subject }}
        <button @click="$emit('mailSelected', mail)">View</button>
        <br />
        ({{ formatDate(mail.sendDate) }})
      </li>
    </ul>
  </div>
</template>

<script>
import { formatDate } from "../date";

export default {
  name: "MailList",
  props: ["mails"],
  methods: { formatDate }
};
</script>

<style scoped></style>
