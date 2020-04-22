<template>
  <div>
    <h1>Mail View</h1>
    <p>
      ID: <strong>{{ mail.id }}</strong>
    </p>
    <p>
      Subject: <strong>{{ mail.subject }}</strong>
    </p>
    <p>
      Send-date: <strong>{{ formatDate(mail.sendDate) }}</strong>
    </p>
    <p>
      Received-date: <strong>{{ formatDate(mail.receivedDate) }}</strong>
    </p>

    <div v-if="mail.to.length">
      <p>To:</p>
      <ul>
        <li v-for="to in mail.to" :key="JSON.stringify(to)">
          <span v-if="to.name">{{ to.name }} ({{ to.email }})</span>
          <span v-else>{{ to.email }}</span>
        </li>
      </ul>
    </div>
    <div v-if="mail.cc.length">
      <p>CC:</p>
      <ul>
        <li v-for="to in mail.cc" :key="JSON.stringify(to)">
          <span v-if="to.name">{{ to.name }} ({{ to.email }})</span>
          <span v-else>{{ to.email }}</span>
        </li>
      </ul>
    </div>
    <div v-if="mail.bcc.length">
      <p>BCC:</p>
      <ul>
        <li v-for="to in mail.bcc" :key="JSON.stringify(to)">
          <span v-if="to.name">{{ to.name }} ({{ to.email }})</span>
          <span v-else>{{ to.email }}</span>
        </li>
      </ul>
    </div>
    <p>From:</p>
    <ul>
      <li v-for="to in mail.from" :key="JSON.stringify(to)">
        <span v-if="to.name">{{ to.name }} ({{ to.email }})</span>
        <span v-else>{{ to.email }}</span>
      </li>
    </ul>
    <div v-if="mail.replyTo.length /*todo check if  reply to is same as from*/">
      <p>Reply-to:</p>
      <ul>
        <li v-for="to in mail.replyTo" :key="JSON.stringify(to)">
          <span v-if="to.name">{{ to.name }} ({{ to.email }})</span>
          <span v-else>{{ to.email }}</span>
        </li>
      </ul>
    </div>
    <details>
      <summary>Header:</summary>
      <table>
        <tr v-for="h in mail.allHeaders" :key="JSON.stringify(h)">
          <td>{{ h.name }}</td>
          <td>{{ h.value }}</td>
        </tr>
      </table>
    </details>
    <p>Content:</p>
    <pre>
    {{ mail.content }}
      </pre
    >
  </div>
</template>

<script>
import { formatDate } from "../date.js";
export default {
  name: "MailViewer",
  props: ["mail"],
  methods: { formatDate }
};
</script>

<style scoped></style>
