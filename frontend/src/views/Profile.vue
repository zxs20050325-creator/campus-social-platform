<template>
  <div class="profile-container">
    <el-page-header title="返回" @back="$router.back()">
      <template #content>
        <h2>个人中心</h2>
      </template>
    </el-page-header>

    <div class="profile-content">
      <el-card class="user-info-card">
        <div class="user-header">
          <el-avatar :size="80" :src="user.avatarUrl || defaultAvatar">
            {{ user.nickname ? user.nickname.charAt(0) : 'U' }}
          </el-avatar>
          <div class="user-basic-info">
            <h3>{{ user.nickname || '未设置昵称' }}</h3>
            <p>学号: {{ user.studentId || '-' }}</p>
            <p>学校: {{ user.school || '-' }}</p>
          </div>
        </div>

        <div class="user-contact">
          <h4>联系方式</h4>
          <p>{{ user.contactInfo || '暂未设置联系方式' }}</p>
        </div>
      </el-card>

      <el-tabs class="profile-tabs" stretch>
        <el-tab-pane label="我发布的商品">
          <div class="items-grid">
            <el-card 
              v-for="item in myItems" 
              :key="item.id" 
              class="item-card"
              shadow="hover"
            >
              <div class="item-preview">
                <el-image
                  v-if="item.imageUrls && item.imageUrls[0]"
                  :src="item.imageUrls[0]"
                  fit="cover"
                  class="item-image"
                  :preview-src-list="item.imageUrls"
                  preview-teleported
                />
                <div v-else class="no-image">暂无图片</div>
                
                <div class="item-text">
                  <h4>{{ item.title }}</h4>
                  <p class="item-desc">{{ item.description }}</p>
                  
                  <div class="item-footer">
                    <span class="item-price" v-if="item.price">
                      ¥{{ parseFloat(item.price).toFixed(2) }}
                    </span>
                    <span class="item-exchange" v-else-if="item.exchangeIntention">
                      {{ item.exchangeIntention }}
                    </span>
                    
                    <el-tag 
                      :type="item.status === 'ON_SHELF' ? 'success' : 'info'"
                      size="small"
                    >
                      {{ item.status === 'ON_SHELF' ? '在售' : '已下架' }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </el-card>
            
            <div v-if="myItemsLoading" class="loading-more">
              <el-skeleton :rows="4" animated />
            </div>
            
            <div v-if="myItems.length === 0 && !myItemsLoading" class="empty-state">
              <el-empty description="暂无发布商品" />
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="我发布的帖子">
          <div class="posts-list">
            <el-card 
              v-for="post in myPosts" 
              :key="post.id" 
              class="post-card"
              shadow="hover"
            >
              <div class="post-content">
                <p class="post-text">{{ post.content }}</p>
                
                <div v-if="post.imageUrls && post.imageUrls.length > 0" class="post-images">
                  <el-image 
                    v-for="(img, index) in post.imageUrls.slice(0, 3)" 
                    :key="index"
                    :src="img"
                    fit="cover"
                    class="post-image"
                    :preview-src-list="post.imageUrls"
                    preview-teleported
                  />
                </div>
                
                <div class="post-meta">
                  <span class="post-time">{{ formatDate(post.createdAt) }}</span>
                  <span class="post-likes">赞({{ post.likeCount || 0 }})</span>
                </div>
              </div>
            </el-card>
            
            <div v-if="myPostsLoading" class="loading-more">
              <el-skeleton :rows="4" animated />
            </div>
            
            <div v-if="myPosts.length === 0 && !myPostsLoading" class="empty-state">
              <el-empty description="暂无发布帖子" />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: 'Profile',
  data() {
    return {
      user: {},
      myItems: [],
      myPosts: [],
      myItemsLoading: false,
      myPostsLoading: false,
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    };
  },
  async mounted() {
    await this.fetchUserInfo();
    await this.fetchMyItems();
    await this.fetchMyPosts();
  },
  methods: {
    async fetchUserInfo() {
      // 在实际应用中，这里应该调用获取用户信息的API
      // 目前模拟数据
      this.user = {
        nickname: '张三',
        studentId: '2021001',
        school: '某某大学',
        contactInfo: '微信: zhangsan123',
        avatarUrl: null
      };
    },
    async fetchMyItems() {
      this.myItemsLoading = true;
      try {
        const response = await request.get('/items/my-items', {
          params: { page: 0, size: 10 }
        });
        this.myItems = response.items || [];
      } catch (error) {
        console.error('获取我的商品失败:', error);
        this.$message.error('获取我的商品失败');
      } finally {
        this.myItemsLoading = false;
      }
    },
    async fetchMyPosts() {
      this.myPostsLoading = true;
      try {
        const response = await request.get('/posts/my-posts', {
          params: { page: 0, size: 10 }
        });
        this.myPosts = response.posts || [];
      } catch (error) {
        console.error('获取我的帖子失败:', error);
        this.$message.error('获取我的帖子失败');
      } finally {
        this.myPostsLoading = false;
      }
    },
    formatDate(dateString) {
      if (!dateString) return '';
      
      // 处理ISO 8601格式的日期字符串
      let date = new Date(dateString);
      
      // 如果无法解析日期，则返回原始值
      if (isNaN(date.getTime())) {
        return dateString;
      }
      
      const now = new Date();
      const diffMs = now - date;
      const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));
      
      if (diffDays === 0) {
        return '今天';
      } else if (diffDays === 1) {
        return '昨天';
      } else if (diffDays < 7) {
        return `${diffDays}天前`;
      } else {
        // 格式化为 YYYY-MM-DD
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
      }
    }
  }
};
</script>

<style scoped>
.profile-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.user-info-card {
  margin-bottom: 20px;
}

.user-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
}

.user-basic-info h3 {
  margin: 0 0 5px 0;
}

.user-basic-info p {
  margin: 3px 0;
  color: #666;
}

.user-contact h4 {
  margin: 0 0 10px 0;
  color: #333;
}

.user-contact p {
  margin: 0;
  color: #666;
}

.profile-tabs {
  margin-top: 20px;
}

.items-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.item-card {
  border-radius: 8px;
}

.item-preview {
  display: flex;
}

.item-image {
  width: 100px;
  height: 100px;
  border-radius: 4px;
  margin-right: 15px;
}

.no-image {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  color: #999;
  border-radius: 4px;
  margin-right: 15px;
}

.item-text {
  flex: 1;
}

.item-text h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-desc {
  font-size: 14px;
  color: #666;
  margin: 0 0 10px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-price {
  font-weight: bold;
  color: #ff6b35;
}

.item-exchange {
  color: #1890ff;
  font-size: 14px;
}

.posts-list {
  margin-top: 20px;
}

.post-card {
  margin-bottom: 15px;
  border-radius: 8px;
}

.post-text {
  margin: 0 0 10px 0;
  line-height: 1.6;
}

.post-images {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.post-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  color: #999;
  font-size: 14px;
}

.loading-more {
  grid-column: 1 / -1;
  padding: 20px;
}

.empty-state {
  grid-column: 1 / -1;
  padding: 40px 0;
}

@media (max-width: 768px) {
  .profile-container {
    padding: 10px;
  }
  
  .user-header {
    flex-direction: column;
    text-align: center;
  }
  
  .items-grid {
    grid-template-columns: 1fr;
  }
}
</style>