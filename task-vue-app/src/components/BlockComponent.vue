<template>
  <div class="block" :style="blockStyle">
    <div class="block-header">
      <span class="block-title">Блок {{ blockIndex }}</span>
      <span class="block-id">ID: {{ blockId }}</span>
    </div>
    <div class="block-content">
      <!-- Здесь будет динамическое содержимое в зависимости от данных с сервера -->
      <p class="placeholder-text">
        Колонка {{ columnId }}, Блок {{ blockIndex }}
      </p>
    </div>
    <div class="block-footer">
      <span class="block-status">Активен</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BlockComponent',
  props: {
    blockId: {
      type: String,
      required: true
    },
    columnId: {
      type: Number,
      required: true
    },
    blockIndex: {
      type: Number,
      required: true
    }
  },
  computed: {
    // Разные цвета для разных колонок (для визуального различия)
    blockStyle() {
      const colors = [
        '#e3f2fd', '#f3e5f5', '#e8f5e8', '#fff3e0',
        '#e0f2f1', '#fce4ec', '#e8eaf6'
      ]
      return {
        borderLeft: `4px solid ${colors[this.columnId - 1] || '#2196f3'}`,
        backgroundColor: colors[this.columnId - 1] || '#f5f5f5'
      }
    }
  },
  mounted() {
    // Здесь будет логика загрузки данных для блока
    // console.log(`Блок ${this.blockId} загружен`);
  }
}
</script>

<style scoped>
.block {
  background: white;
  border-radius: 6px;
  padding: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
  border: 1px solid #e0e0e0;
}

.block:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.block-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 6px;
  border-bottom: 1px dashed #e0e0e0;
}

.block-title {
  font-weight: 600;
  color: #2c3e50;
}

.block-id {
  font-size: 0.75rem;
  color: #999;
  font-family: monospace;
}

.block-content {
  min-height: 60px;
  margin-bottom: 10px;
}

.placeholder-text {
  margin: 0;
  color: #666;
  font-size: 0.9rem;
  text-align: center;
  padding: 8px 0;
}

.block-footer {
  padding-top: 6px;
  border-top: 1px solid #f0f0f0;
  font-size: 0.8rem;
}

.block-status {
  color: #4caf50;
  font-weight: 500;
  display: inline-block;
  padding: 2px 8px;
  background-color: #e8f5e8;
  border-radius: 12px;
}
</style>