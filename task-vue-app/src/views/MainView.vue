<template>
  <div class="main-container">
    <aside class="sidebar">
      <div class="sidebar-content">
        <div class="spacer"></div>
        <div class="user-info">
          <div class="user-details">
            <p class="username">{{ authStore.user }}</p>
          </div>
          <button @click="handleLogout" class="logout-btn">
            Выйти
          </button>
        </div>
      </div>
    </aside>

    <main class="main-content">
      <div class="content-wrapper">
        <DashboardComponent/>
      </div>
    </main>
  </div>
</template>

<script setup>
import {useRouter} from "vue-router";
import {useAuthStore} from "@/stores/auth.js";
import DashboardComponent from "@/components/DashboardComponent.vue";

const router = useRouter();
const authStore = useAuthStore();

const handleLogout = () => {
  console.log('called handleLogout...')
  authStore.logout();
  router.push('/login');
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