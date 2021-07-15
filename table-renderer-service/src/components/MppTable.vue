<template>
  <table class="styled-table" v-if="jsonFile">
    <thead>
      <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Duration</th>
        <th>Cost</th>
        <th>Work</th>
        <th>Start</th>
        <th>Finish</th>
        <th>Predecessors</th>
        <th>Resource Names</th>
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="item in tasks.filter((tk) => tk.id > 0)"
        :key="item.id"
        :style="itemStyle(item)"
      >
        <td>{{ item.id }}</td>
        <td :style="alignStyle(item)">
            <span>{{ item.name }}</span>
        </td>
        <td>
          {{ Math.round(item.duration.duration * 100) / 100 }}
          {{ item.duration.units }}
        </td>
        <td>{{ Math.round(item.cost * 100) / 100 }}</td>
        <td>{{ item.work.duration }} {{ item.work.units }}</td>
        <td>
          {{ getStartDate(item) }}
        </td>
        <td>
          {{ getFinishDate(item) }}
        </td>
        <td>
          <span v-if="item.predecessors.length">{{
            item.predecessors.map((p) => p.id).join(", ")
          }}</span>
        </td>
        <td>{{ getResourceName(item) }}</td>
      </tr>
    </tbody>
  </table>
</template>


<script>
export default {
  name: "mpp-table",
  components: {},
  props: ["jsonFile"],
  data() {
    return {
      tasks: null,
      assignments: null,
      resources: null,
    };
  },
  created: function () {
    this.tasks = this.jsonFile.tasks;
    this.resources = this.jsonFile.resources;
    this.assignments = this.jsonFile.assignments;
  },
  methods: {
    getResourceName: function (task) {
      let matchedAssignments = this.assignments.filter(
        (a) => a.taskId === task.id
      );
      if (!matchedAssignments || !matchedAssignments.length) return "";

      let resourceNames = [];
      for (let a of matchedAssignments) {
        let resource = this.resources.find((r) => r.id === a.resourceId);
        if (!resource) break;
        resourceNames.push(resource.name);
      }

      return resourceNames.join(", ");
    },
    getLevelOfTask: function (task, tasks) {
      if (!task.parentTaskId) return 0;
      let parentTask = tasks.find((tk) => tk.id === task.parentTaskId);
      return this.getLevelOfTask(parentTask, tasks) + 1;
    },
    itemStyle: function (task) {
      return !task.childTaskIds.length ? "" : "font-weight:bold;color:#009879;";
    },
    getFinishDate(task) {
      return new Intl.DateTimeFormat("en-US", {
        weekday: "long",
        year: "numeric",
        month: "numeric",
        day: "numeric",
      }).format(new Date(task.finish));
    },
    getStartDate(task) {
      return new Intl.DateTimeFormat("en-US", {
        weekday: "long",
        year: "numeric",
        month: "numeric",
        day: "numeric",
      }).format(new Date(task.start));
    },
    alignStyle(task) {
      let level = this.getLevelOfTask(task, this.tasks);
      return "position: relative;padding-left:" + level * 12 + "px";
    }
  },
};
</script>

<style scoped>
.styled-table {
  border-collapse: collapse;
  margin: 25px auto;
  font-size: 0.9em;
  font-family: sans-serif;
  min-width: 600px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
}

.styled-table thead tr {
  background-color: #009879;
  color: #ffffff;
  
  text-align: left;
}

.styled-table th,
.styled-table td {
  padding: 12px 15px;
}

.styled-table tbody tr {
  border-bottom: 1px solid #dddddd;
  text-align: left;
}

.styled-table tbody tr:nth-of-type(even) {
  background-color: #f3f3f3;
}

.styled-table tbody tr:last-of-type {
  border-bottom: 2px solid #009879;
}
</style>