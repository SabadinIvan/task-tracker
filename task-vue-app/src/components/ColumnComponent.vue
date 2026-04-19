<template>
  <div class="column">
    <h3 class="column-header">Колонка {{ columnId }}</h3>
    <div class="blocks-container">
      <BlockComponent
          v-for="index in blocksCount"
          :key="`${columnId}-${index}`"
          :block-id="`${columnId}-${index}`"
          :column-id="columnId"
          :block-index="index"
      />
    </div>
  </div>
</template>

<script>
import BlockComponent from './BlockComponent.vue'

export default {
  name: 'ColumnComponent',
  components: {
    BlockComponent
  },
  props: {
    columnId: {
      type: Number,
      required: true
    },
    blocksCount: {
      type: Number,
      default: 1,
      validator: value => value >= 1 && value <= 9
    }
  }
}
</script>

<style scoped>
.column {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 12px;
  display: flex;
  flex-direction: column;
  height: fit-content;
}

.column-header {
  margin: 0 0 12px 0;
  padding-bottom: 8px;
  border-bottom: 2px solid #e0e0e0;
  color: #333;
  font-size: 1.1rem;
  font-weight: 600;
  text-align: center;
}

.blocks-container {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
</style>