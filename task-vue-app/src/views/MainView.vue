<template>
  <Sidebar
      :user-name="userName"
      :user-logon="userLogon"
      @toggle="onSidebarToggle"
      @filter-change="onFilterChange"
      @task-created="onTaskCreated"
      @logout="handleLogout"
  />

  <div class="main-container">
    <main class="main-content">
      <div class="content-wrapper">
        <DashboardComponent/>
      </div>
    </main>
  </div>
</template>

<script>
import {useAuthStore} from "@/stores/auth.js";
import DashboardComponent from "@/components/DashboardComponent.vue";
import Sidebar from "@/components/Sidebar.vue";
import taskService from '../services/taskService'

export default {
  name: 'MainView',
  components: {Sidebar, DashboardComponent},
  data() {
    return {
      isSidebarCollapsed: false,
      tasks: [],
      filters: {
        search: '',
        status: '',
        sortBy: 'date_desc'
      },
      userName: '',
      userLogon: ''
    }
  },
  computed: {},
  methods: {
    onSidebarToggle(collapsed) {
      this.isSidebarCollapsed = collapsed
    },
    onFilterChange(filters) {
      console.log('called onFilterChange; filters -> ' + filters);
      this.filters = filters
    },
    async onTaskCreated(newTask) {
      console.log('called onTaskCreated; newTask -> ' + newTask);
      await this.loadTasks()
    },
    async loadTasks() {
      console.log('called loadTasks ....');
      const result = await taskService.getTasks();
      if (result.success) {
        this.tasks = result.data
      } else {
        console.error('Ошибка загрузки задач:', result.error)
      }
      this.showTasks();
    },
    loadUser() {
      const authStore = useAuthStore();
      this.userName = authStore.getFullUserName();
      this.userLogon = authStore.getUserName();

    },
    handleLogout() {
      console.log('called handleLogout...')
      const authStore = useAuthStore();
      authStore.logout();
      this.$router.push('/login');
    },
    showTasks() {
      console.log(this.tasks);
    }
  },
  mounted() {
    this.loadUser();
    this.loadTasks();
  }
}
</script>

<style scoped>
.main-container {
  display: flex;
  background-color: #f5f6fa;
  min-height: 100vh;
}

.sidebar {
  width: 10%;
  max-width: 10%;
  background-color: #2c3e50;
  color: white;
  position: fixed;
  height: 100vh;
  overflow-y: auto;
}

.sidebar-content {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 20px;
}

.spacer {
  flex: 1;
}

.user-info {
  border-top: 1px solid #40556b;
  padding-top: 20px;
}

.user-details {
  margin-bottom: 15px;
}

.username {
  font-weight: 600;
  font-size: 16px;
  word-break: break-word;
}

.logout-btn {
  width: 100%;
  padding: 10px;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.3s;
}

.logout-btn:hover {
  background-color: #c0392b;
}

.main-content {
  width: 90%;
  margin-left: 10%;
  /*background-color: #f5f6fa;*/
  min-height: 100vh;
}

.content-wrapper {
  padding: 40px;
}

.content-wrapper h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.content-wrapper p {
  color: #7f8c8d;
  font-size: 18px;
  margin-bottom: 30px;
}

.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.card {
  background: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 20px rgba(0,0,0,0.15);
}

.card h3 {
  color: #2c3e50;
  margin-bottom: 10px;
}

.card p {
  color: #7f8c8d;
  font-size: 14px;
  margin-bottom: 0;
}
</style>