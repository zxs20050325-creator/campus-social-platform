<template>
  <div class="login-container">
    <div class="login-form">
      <h2>校园社交平台登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="学号" prop="username">
          <el-input 
            v-model="form.username" 
            placeholder="请输入学号"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="form.password" 
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleLogin"
            :loading="loading"
            style="width: 100%"
          >
            登录
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button 
            type="info" 
            @click="handleRegister"
            plain
            style="width: 100%"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入学号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度至少6位', trigger: 'blur' }
        ]
      },
      loading: false
    };
  },
  methods: {
    async handleLogin() {
      try {
        await this.$refs.formRef.validate();
        this.loading = true;
        
        const response = await request.post('/auth/login', {
          username: this.form.username,
          password: this.form.password
        });
        
        if (response.token) {
          localStorage.setItem('token', response.token);
          this.$message.success('登录成功');
          this.$router.push('/');
        } else {
          this.$message.error('登录失败');
        }
      } catch (error) {
        console.error('登录错误:', error);
        this.$message.error(error.message || '登录失败');
      } finally {
        this.loading = false;
      }
    },
    async handleRegister() {
      try {
        await this.$refs.formRef.validateField(['username', 'password']);
        
        this.loading = true;
        
        const response = await request.post('/auth/register', {
          studentId: this.form.username,
          nickname: this.form.username,
          password: this.form.password,
          school: '校园社交平台',
          contactInfo: '暂无'
        });
        
        if (response.userId) {
          this.$message.success('注册成功，请登录');
        } else {
          this.$message.error('注册失败');
        }
      } catch (error) {
        console.error('注册错误:', error);
        this.$message.error(error.message || '注册失败');
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-form {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.login-form h2 {
  text-align: center;
  margin-bottom: 20px;
}
</style>