<template>
  <div v-if="json">
    <mpp-table
      :jsonFile="json"
    />
  </div>
</template>

<script>
import MppTable from "./components/MppTable.vue";

export default {
  name: "App",
  components: {
    MppTable,
  },
  data: function() {
    return {
      json: null,
    };
  },
  created: function() {
    this.json = require('./assets/data.json');
  },
};
</script>

