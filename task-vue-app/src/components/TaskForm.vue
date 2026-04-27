<template>
  <div class="modal-overlay" @click.self="handleClose">
    <div class="task-container">
      <div class="task-card">
        <div class="task-header">
          <h2>Создание новой задачи</h2>
          <p>Заполните информацию о задаче</p>
          <button class="close-btn" @click="handleClose">×</button>
        </div>

        <form @submit.prevent="handleCreateTask" class="task-form">
          <!-- Заголовок задачи -->
          <div class="form-group">
            <label for="title">Название задачи *</label>
            <input
                type="text"
                id="title"
                v-model="form.title"
                @blur="validateField('title')"
                :class="{ 'error': errors.title }"
                placeholder="Введите название задачи"
                maxlength="100"
                autofocus
            />
            <div class="field-footer" v-if="!errors.title">
              <span class="char-counter">{{ form.title.length }}/100</span>
            </div>
            <span class="error-message" v-if="errors.title">{{ errors.title }}</span>
          </div>

          <!-- Описание задачи -->
          <div class="form-group">
            <label for="description">Описание задачи *</label>
            <textarea
                id="description"
                v-model="form.description"
                @blur="validateField('description')"
                :class="{ 'error': errors.description }"
                placeholder="Введите описание задачи"
                rows="6"
                maxlength="1000"
            ></textarea>
            <div class="field-footer" v-if="!errors.description">
              <span class="char-counter">{{ form.description.length }}/1000</span>
            </div>
            <span class="error-message" v-if="errors.description">{{ errors.description }}</span>
          </div>

          <!-- Сообщения -->
          <div class="message" v-if="message" :class="messageType">
            {{ message }}
          </div>

          <!-- Кнопки действий -->
          <div class="button-group">
            <button
                type="button"
                class="cancel-btn"
                @click="handleClose"
                :disabled="loading"
            >
              Отмена
            </button>
            <button
                type="submit"
                class="submit-btn"
                :disabled="loading || !isFormValid"
            >
              <span v-if="loading">Создание...</span>
              <span v-else>Создать задачу</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import taskService from '../services/taskService'

export default {
  name: 'CreateTask',
  props: {
    visible: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      form: {
        title: '',
        description: ''
      },
      errors: {},
      loading: false,
      message: '',
      messageType: ''
    }
  },
  computed: {
    isFormValid() {
      // console.log('title -> ' + this.form.title.trim());
      // console.log('description -> ' + this.form.description.trim());
      // console.log('length -> ' + Object.keys(this.errors).length);
      return this.form.title.trim() &&
          this.form.description.trim(); //&&
          // Object.keys(this.errors).length === 0;
    }
  },
  methods: {
    validateField(fieldName) {
      const value = this.form[fieldName].trim()
      this.errors[fieldName] = ''

      switch(fieldName) {
        case 'title':
          if (!value) {
            this.errors.title = 'Название задачи обязательно'
          } else if (value.length < 3) {
            this.errors.title = 'Название должно содержать минимум 3 символа'
          } else if (value.length > 100) {
            this.errors.title = 'Название не должно превышать 100 символов'
          }
          // console.log('errors.title -> ' + this.errors.title)
          break

        case 'description':
          if (!value) {
            this.errors.description = 'Описание задачи обязательно'
          } else if (value.length < 10) {
            this.errors.description = 'Описание должно содержать минимум 10 символов'
          } else if (value.length > 1000) {
            this.errors.description = 'Описание не должно превышать 1000 символов'
          }
          // console.log('errors.description -> ' + this.errors.description)
          break
      }
    },

    async handleCreateTask() {
      // Валидация всех полей
      this.validateField('title')
      this.validateField('description')

      if (!this.isFormValid) {
        this.showMessage('Пожалуйста, заполните все поля корректно', 'error')
        return
      }

      this.loading = true
      // this.showMessage('Создание задачи...', 'info')

      console.log('title -> ' + this.form.title);
      console.log('description -> ' + this.form.description);

      this.loading = false

      // Используем сервис для создания задачи
      const result = await taskService.createTask(this.form)

      if (result.success) {
        this.showMessage('Задача успешно создана!', 'success')

        // Эмитим событие с созданной задачей
        this.$emit('task-created', result.data)

        // Очищаем форму
        this.resetForm()

        // Закрываем модальное окно через 1 секунду
        setTimeout(() => {
          this.handleClose()
        }, 1000)
      } else {
        // Обработка ошибок из сервиса
        this.showMessage(result.error, 'error')

        if (result.errors) {
          this.errors = { ...this.errors, ...result.errors }
        }
      }

      this.loading = false
    },

    handleClose() {
      this.resetForm()
      this.message = ''
      this.$emit('close')
    },

    resetForm() {
      this.form = {
        title: '',
        description: ''
      }
      this.errors = {}
    },

    showMessage(text, type) {
      this.message = text
      this.messageType = type

      // Автоматически скрыть сообщение через 5 секунд
      if (type !== 'info') {
        setTimeout(() => {
          if (this.message === text) {
            this.message = ''
          }
        }, 5000)
      }
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

.task-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  width: 100%;
  max-width: 600px;
}

.task-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  padding: 40px;
  width: 100%;
  position: relative;
  animation: slideUp 0.3s ease;
}

.task-header {
  text-align: center;
  margin-bottom: 32px;
  position: relative;
}

.task-header h2 {
  margin: 0;
  color: #333;
  font-size: 28px;
  font-weight: 600;
}

.task-header p {
  margin: 8px 0 0;
  color: #666;
  font-size: 16px;
}

.close-btn {
  position: absolute;
  top: -20px;
  right: -20px;
  background: white;
  border: none;
  font-size: 32px;
  cursor: pointer;
  color: #999;
  transition: all 0.2s;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.close-btn:hover {
  color: #333;
  transform: scale(1.1);
}

.task-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-weight: 600;
  color: #444;
  font-size: 14px;
}

.form-group input,
.form-group textarea {
  padding: 12px 16px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 15px;
  transition: all 0.3s;
  background: white;
  font-family: inherit;
}

.form-group textarea {
  resize: vertical;
  min-height: 120px;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.form-group input.error,
.form-group textarea.error {
  border-color: #f44336;
  background-color: #fff8f8;
}

.form-group input.error:focus,
.form-group textarea.error:focus {
  border-color: #f44336;
  box-shadow: 0 0 0 3px rgba(244, 67, 54, 0.1);
}

.field-footer {
  display: flex;
  justify-content: flex-end;
}

.char-counter {
  font-size: 12px;
  color: #999;
}

.error-message {
  color: #f44336;
  font-size: 12px;
  margin-top: 4px;
}

.message {
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 14px;
  animation: slideIn 0.3s ease;
}

.message.error {
  background-color: #ffebee;
  color: #c62828;
  border-left: 4px solid #f44336;
}

.message.success {
  background-color: #e8f5e8;
  color: #2e7d32;
  border-left: 4px solid #4caf50;
}

.message.info {
  background-color: #e3f2fd;
  color: #1565c0;
  border-left: 4px solid #2196f3;
}

.button-group {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.submit-btn,
.cancel-btn {
  flex: 1;
  padding: 14px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
}

.submit-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.cancel-btn {
  background: #f5f5f5;
  color: #666;
  border: 2px solid #e0e0e0;
}

.cancel-btn:hover:not(:disabled) {
  background: #e0e0e0;
  border-color: #ccc;
  transform: translateY(-2px);
}

.cancel-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
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

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Адаптивность */
@media (max-width: 600px) {
  .task-card {
    padding: 24px;
    margin: 16px;
  }

  .task-header h2 {
    font-size: 24px;
  }

  .button-group {
    flex-direction: column-reverse;
  }

  .submit-btn,
  .cancel-btn {
    padding: 12px 20px;
  }

  .close-btn {
    top: -10px;
    right: -10px;
    width: 32px;
    height: 32px;
    font-size: 24px;
  }
}
</style>