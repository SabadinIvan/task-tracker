<template>
  <div class="sidebar" :class="{ 'collapsed': isCollapsed }">
    <div class="sidebar-header">
      <div class="logo" v-if="!isCollapsed">
        <span class="logo-icon">📋</span>
        <span class="logo-text">TaskManager</span>
      </div>
      <div class="logo-icon-only" v-else>
        📋
      </div>
      <button class="toggle-btn" @click="toggleSidebar">
        <span class="toggle-icon">{{ isCollapsed ? '→' : '←' }}</span>
      </button>
    </div>

    <!-- Блок работы с задачами -->
    <div class="sidebar-section tasks-section">
      <div class="section-header" v-if="!isCollapsed">
        <span class="section-icon">✅</span>
        <span class="section-title">Работа с задачами</span>
      </div>
      <div class="section-header-collapsed" v-else title="Работа с задачами">
        ✅
      </div>

      <!-- Кнопка создания задачи -->
      <button
          class="sidebar-btn create-btn"
          @click="openCreateTaskModal"
          :class="{ 'collapsed': isCollapsed }"
      >
        <span class="btn-icon">+</span>
        <span v-if="!isCollapsed" class="btn-text">Создать задачу</span>
      </button>

      <!-- Поиск по задачам -->
      <div class="search-section">
        <div class="search-input-wrapper">
          <span class="search-icon">🔍</span>
          <input
              type="text"
              v-model="searchQuery"
              :placeholder="isCollapsed ? 'Поиск' : 'Поиск задач...'"
              class="search-input"
              @input="handleSearch"
          />
        </div>

        <!-- Расширенный поиск (показывается только когда sidebar открыт) -->
        <div v-if="!isCollapsed" class="filters">
          <select v-model="filterStatus" class="filter-select" @change="applyFilters">
            <option value="">Все статусы</option>
            <option value="pending">Ожидает</option>
            <option value="in_progress">В работе</option>
            <option value="completed">Завершено</option>
          </select>

          <select v-model="sortBy" class="filter-select" @change="applyFilters">
            <option value="date_desc">По дате (новые)</option>
            <option value="date_asc">По дате (старые)</option>
            <option value="title_asc">По названию (А-Я)</option>
            <option value="title_desc">По названию (Я-А)</option>
          </select>
        </div>
      </div>

      <!-- Быстрые фильтры -->
      <div v-if="!isCollapsed" class="quick-filters">
        <button
            class="filter-chip"
            :class="{ active: activeFilter === 'all' }"
            @click="setQuickFilter('all')"
        >
          Все
        </button>
        <button
            class="filter-chip"
            :class="{ active: activeFilter === 'pending' }"
            @click="setQuickFilter('pending')"
        >
          Ожидают
        </button>
        <button
            class="filter-chip"
            :class="{ active: activeFilter === 'in_progress' }"
            @click="setQuickFilter('in_progress')"
        >
          В работе
        </button>
        <button
            class="filter-chip"
            :class="{ active: activeFilter === 'completed' }"
            @click="setQuickFilter('completed')"
        >
          Завершены
        </button>
      </div>
    </div>

    <!-- Блок пользователя (снизу) -->
    <div class="sidebar-section user-section">
      <div class="user-info" @click="openUserModal">
        <div class="user-avatar">
          {{ getUserInitials() }}
        </div>
        <div v-if="!isCollapsed" class="user-details">
          <div class="user-name">{{ userName }}</div>
          <div class="user-email">{{ userLogon }}</div>
        </div>
        <div v-else class="avatar-only" :title="userName">
          👤
        </div>
      </div>

      <button
          class="sidebar-btn logout-btn"
          @click="handleLogout"
          :class="{ 'collapsed': isCollapsed }"
      >
        <span class="btn-icon">🚪</span>
        <span v-if="!isCollapsed" class="btn-text">Выйти</span>
      </button>
    </div>

    <!-- Модальное окно информации о пользователе -->
    <UserModal
        v-if="showUserModal"
        :user="userData"
        @close="closeUserModal"
    />

    <!-- Модальное окно создания задачи -->
    <TaskForm
        v-if="showCreateModal"
        @close="closeCreateTaskModal"
        @task-created="onTaskCreated"
    />
  </div>
</template>

<script>
import TaskForm from './TaskForm.vue'
import UserModal from './UserModal.vue'

export default {
  name: 'Sidebar',
  components: {
    TaskForm,
    UserModal
  },
  props: {
    userName: {
      type: String,
      default: 'Пользователь'
    },
    userLogon: {
      type: String,
      default: 'user@example.com'
    }
  },
  data() {
    return {
      isCollapsed: false,
      showUserModal: false,
      showCreateModal: false,
      searchQuery: '',
      filterStatus: '',
      sortBy: 'date_desc',
      activeFilter: 'all',
      searchTimeout: null
    }
  },
  computed: {
    userData() {
      return {
        name: this.userName,
        email: this.userLogon,
        avatar: this.getUserInitials(),
        registeredAt: '2024-01-01',
        tasksCount: 0
      }
    }
  },
  methods: {
    toggleSidebar() {
      console.log('called toggleSidebar...')
      this.isCollapsed = !this.isCollapsed
      // Сохраняем состояние в localStorage
      localStorage.setItem('sidebarCollapsed', this.isCollapsed)
      this.$emit('toggle', this.isCollapsed)
    },

    getUserInitials() {
      return this.userName
          .split(' ')
          .map(word => word[0])
          .join('')
          .toUpperCase()
          .slice(0, 2)
    },

    openCreateTaskModal() {
      this.showCreateModal = true
      document.body.style.overflow = 'hidden'
    },

    closeCreateTaskModal() {
      this.showCreateModal = false
      document.body.style.overflow = ''
    },

    openUserModal() {
      this.showUserModal = true
      document.body.style.overflow = 'hidden'
    },

    closeUserModal() {
      this.showUserModal = false
      document.body.style.overflow = ''
    },

    onTaskCreated(newTask) {
      this.$emit('task-created', newTask)
      this.closeCreateTaskModal()
    },

    handleSearch() {
      // Дебаунс для поиска
      clearTimeout(this.searchTimeout)
      this.searchTimeout = setTimeout(() => {
        this.applyFilters()
      }, 300)
    },

    applyFilters() {
      const filters = {
        search: this.searchQuery,
        status: this.filterStatus,
        sortBy: this.sortBy
      }
      this.$emit('filter-change', filters)
    },

    setQuickFilter(filter) {
      this.activeFilter = filter
      this.filterStatus = filter === 'all' ? '' : filter
      this.applyFilters()
    },

    handleLogout() {
      this.$emit('logout')
    }
  },
  mounted() {
    // Восстанавливаем состояние sidebar
    const savedState = localStorage.getItem('sidebarCollapsed')
    if (savedState !== null) {
      this.isCollapsed = savedState === 'true'
      this.$emit('toggle', this.isCollapsed)
    }
  }
}
</script>

<style scoped>
.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  background: linear-gradient(180deg, #ffffff 0%, #f8f9fa 100%);
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 100;
  overflow-y: auto;
  overflow-x: hidden;
}

.sidebar:not(.collapsed) {
  width: 280px;
}

.sidebar.collapsed {
  width: 70px;
}

/* Scrollbar styling */
.sidebar::-webkit-scrollbar {
  width: 4px;
}

.sidebar::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.sidebar::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 2px;
}

.sidebar::-webkit-scrollbar-thumb:hover {
  background: #555;
}

/* Header */
.sidebar-header {
  padding: 20px 16px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  font-size: 28px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  /*background-clip: text;*/
}

.logo-icon-only {
  font-size: 28px;
  text-align: center;
  width: 100%;
}

.toggle-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.toggle-btn:hover {
  background: #f0f0f0;
}

.toggle-icon {
  font-size: 18px;
  color: #666;
}

/* Sections */
.sidebar-section {
  padding: 20px 16px;
}

.tasks-section {
  flex: 1;
  border-bottom: 1px solid #e0e0e0;
}

.user-section {
  margin-top: auto;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid #e0e0e0;
}

.section-header-collapsed {
  text-align: center;
  font-size: 24px;
  margin-bottom: 16px;
  cursor: pointer;
}

.section-icon {
  font-size: 18px;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #444;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Buttons */
.sidebar-btn {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 500;
  margin-bottom: 12px;
}

.sidebar-btn.collapsed {
  justify-content: center;
  padding: 12px;
}

.create-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.create-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.logout-btn {
  background: #f5f5f5;
  color: #666;
  margin-top: 12px;
}

.logout-btn:hover {
  background: #ffebee;
  color: #f44336;
}

.btn-icon {
  font-size: 18px;
}

.btn-text {
  font-size: 14px;
}

/* Search */
.search-section {
  margin: 16px 0;
}

.search-input-wrapper {
  position: relative;
  margin-bottom: 12px;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 16px;
  color: #999;
}

.search-input {
  width: 100%;
  padding: 10px 10px 10px 36px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.2s;
}

.sidebar.collapsed .search-input {
  padding: 10px;
  text-align: center;
}

.sidebar.collapsed .search-icon {
  display: none;
}

.search-input:focus {
  outline: none;
  border-color: #667eea;
}

/* Filters */
.filters {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  background: white;
  font-size: 13px;
  cursor: pointer;
}

.filter-select:focus {
  outline: none;
  border-color: #667eea;
}

/* Quick filters */
.quick-filters {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-top: 12px;
}

.filter-chip {
  padding: 6px 12px;
  background: #f5f5f5;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.filter-chip:hover {
  background: #e0e0e0;
}

.filter-chip.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-color: transparent;
}

/* User info */
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.user-info:hover {
  background: #f0f0f0;
}

.user-avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  font-size: 16px;
}

.avatar-only {
  font-size: 24px;
  text-align: center;
  width: 100%;
}

.user-details {
  flex: 1;
}

.user-name {
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.user-email {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}

/* Responsive */
@media (max-width: 768px) {
  .sidebar:not(.collapsed) {
    width: 100%;
    max-width: 300px;
  }
}
</style>