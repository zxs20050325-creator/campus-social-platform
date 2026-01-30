<template>
  <div class="home-container">
    <h1>校园社交平台</h1>
    <p>欢迎来到校园轻社交 + 资源置换平台</p>
    
    <div class="quick-links">
      <el-card class="link-card" @click="$router.push('/market')">
        <i class="el-icon-shopping-cart-full"></i>
        <h3>资源市场</h3>
        <p>寻找你需要的物品，或出售闲置资源</p>
      </el-card>
      
      <el-card class="link-card" @click="$router.push('/profile')">
        <i class="el-icon-user"></i>
        <h3>个人中心</h3>
        <p>管理个人信息和发布的资源</p>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home'
};
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

.home-container h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  color: #333;
}

.home-container p {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 40px;
}

.quick-links {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-top: 30px;
  flex-wrap: wrap;
}

.link-card {
  width: 250px;
  cursor: pointer;
  text-align: center;
  transition: transform 0.3s ease;
}

.link-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.link-card i {
  font-size: 3rem;
  color: #409eff;
  margin-bottom: 15px;
}

.link-card h3 {
  margin: 10px 0;
  font-size: 1.3rem;
}

.link-card p {
  color: #666;
  font-size: 0.9rem;
  margin: 0;
}
</style>