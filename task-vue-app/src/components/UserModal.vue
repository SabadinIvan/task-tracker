<template>
  <div class="modal-overlay" @click.self="handleClose">
    <div class="user-modal">
      <div class="modal-header">
        <h2>Информация о пользователе</h2>
        <button class="close-btn" @click="handleClose">×</button>
      </div>

      <div class="modal-content">
        <div class="user-avatar-large">
          {{ user.avatar }}
        </div>

        <div class="user-info-item">
          <label>Имя:</label>
          <span>{{ user.name }}</span>
        </div>

        <div class="user-info-item">
          <label>Email:</label>
          <span>{{ user.email }}</span>
        </div>

        <div class="user-info-item">
          <label>Дата регистрации:</label>
          <span>{{ formatDate(user.registeredAt) }}</span>
        </div>

        <div class="user-info-item">
          <label>Всего задач:</label>
          <span>{{ user.tasksCount }}</span>
        </div>
      </div>

      <div class="modal-footer">
        <button class="close-modal-btn" @click="handleClose">Закрыть</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserModal',
  props: {
    user: {
      type: Object,
      default: () => ({
        name: 'Пользователь',
        email: 'user@example.com',
        avatar: 'П',
        registeredAt: '2024-01-01',
        tasksCount: 0
      })
    }
  },
  methods: {
    handleClose() {
      this.$emit('close')
    },
    formatDate(date) {
      if (!date) return 'Не указана'
      return new Date(date).toLocaleDateString('ru-RU')
    }
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.user-modal {
  background: white;
  border-radius: 16px;
  width: 90%;
  max-width: 400px;
  animation: slideUp 0.3s ease;
  overflow: hidden;
}

.modal-header {
  padding: 20px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 32px;
  cursor: pointer;
  color: white;
  transition: transform 0.2s;
  line-height: 1;
}

.close-btn:hover {
  transform: scale(1.1);
}

.modal-content {
  padding: 24px;
}

.user-avatar-large {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 32px;
  margin: 0 auto 20px;
}

.user-info-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.user-info-item label {
  font-weight: 600;
  color: #666;
  font-size: 14px;
}

.user-info-item span {
  color: #333;
  font-size: 14px;
}

.modal-footer {
  padding: 16px 24px;
  background: #f8f9fa;
  display: flex;
  justify-content: flex-end;
}

.close-modal-btn {
  padding: 8px 24px;
  background: #f5f5f5;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.close-modal-btn:hover {
  background: #e0e0e0;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>