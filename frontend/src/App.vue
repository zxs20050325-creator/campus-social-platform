<template>
  <div id="app">
    <el-container>
      <el-header class="header" v-if="showHeader">
        <div class="nav-container">
          <h1>校园社交平台</h1>
          <div class="nav-links">
            <el-button 
              @click="$router.push('/')" 
              :type="$route.path === '/' ? 'primary' : 'default'"
              plain
            >
              首页
            </el-button>
            <el-button 
              @click="$router.push('/market')" 
              :type="$route.path === '/market' ? 'primary' : 'default'"
              plain
            >
              资源市场
            </el-button>
            <el-button 
              @click="$router.push('/profile')" 
              :type="$route.path === '/profile' ? 'primary' : 'default'"
              plain
              v-if="isLoggedIn"
            >
              个人中心
            </el-button>
            <el-button 
              @click="logout" 
              v-if="isLoggedIn"
              plain
            >
              退出
            </el-button>
            <el-button 
              @click="$router.push('/login')" 
              :type="$route.path === '/login' ? 'primary' : 'default'"
              v-if="!isLoggedIn"
              plain
            >
              登录
            </el-button>
          </div>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    isLoggedIn() {
      return !!localStorage.getItem('token');
    },
    showHeader() {
      return this.$route.path !== '/login';
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('token');
      this.$router.push('/login');
      this.$message.success('已退出登录');
    }
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

.header {
  background-color: #409EFF;
  padding: 0 20px;
}

.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  color: white;
}

.nav-container h1 {
  margin: 0;
  color: white;
}

.nav-links {
  display: flex;
  gap: 10px;
  align-items: center;
}

.main-content {
  padding: 20px;
}
</style>