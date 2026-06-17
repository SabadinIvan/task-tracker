<template>
  <div class="auth-container">
    <div class="auth-cont">
      <div class="auth-card">
        <h1>{{ title }}</h1>
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true
  }
})
</script>

<style scoped>
.auth-container {
  /*min-height: 100vh;*/
  display: flex;
  align-items: center;
  justify-content: center;
  /*background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);*/
  padding: 20px;
  border-radius: 20px;
}

.auth-cont {
  /*min-height: 100vh;*/
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  border-radius: 20px;
}

.auth-card {
  background: white;
  border-radius: 20px;
  padding: 40px;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.auth-card h1 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 32px;
}
</style>