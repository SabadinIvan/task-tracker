<template>
  <div class="dashboard">
    <ColumnComponent
        v-for="column in columns"
        :key="column.id"
        :column-id="column.id"
        :blocks-count="column.blocksCount"
    />
  </div>
</template>

<script>
import ColumnComponent from "@/components/ColumnComponent.vue";

export default {
  name: "DashboardComponent",
  components: {
    ColumnComponent
  },
  data() {
    return {
      columns: [
        { id: 1, blocksCount: 3 },
        { id: 2, blocksCount: 5 },
        { id: 3, blocksCount: 2 },
        { id: 4, blocksCount: 7 },
        { id: 5, blocksCount: 4 },
        { id: 6, blocksCount: 9 },
        { id: 7, blocksCount: 1 }
      ]
    }
  }
}
</script>

<style scoped>
.dashboard {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 16px;
  padding: 20px;
  min-height: 100vh;
  background-color: #f5f5f5;
}

@media (max-width: 1200px) {
  .dashboard {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 768px) {
  .dashboard {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .dashboard {
    grid-template-columns: 1fr;
  }
}
</style>