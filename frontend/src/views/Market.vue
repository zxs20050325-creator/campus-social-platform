<template>
  <div class="market-container">
    <div class="filter-section">
      <el-tag 
        v-for="category in categories" 
        :key="category.value"
        :type="selectedCategory === category.value ? '' : 'info'"
        :effect="selectedCategory === category.value ? 'dark' : 'plain'"
        class="category-tag"
        @click="selectCategory(category.value)"
      >
        {{ category.label }}
      </el-tag>
    </div>

    <div class="items-container">
      <div 
        v-for="item in items" 
        :key="item.id"
        class="item-card-wrapper"
      >
        <el-card class="item-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="item-title">{{ item.title }}</span>
              <span v-if="item.status === 'SOLD_OUT'" class="status-badge sold-out">已售罄</span>
              <span v-else class="status-badge on-shelf">在售</span>
            </div>
          </template>
          
          <div class="item-content">
            <div v-if="item.imageUrls && item.imageUrls.length > 0" class="item-images">
              <el-carousel 
                v-if="item.imageUrls.length > 1" 
                height="150px" 
                indicator-position="none" 
                arrow="never"
                :autoplay="false"
              >
                <el-carousel-item v-for="(img, idx) in item.imageUrls" :key="idx">
                  <el-image 
                    :src="img" 
                    fit="cover" 
                    class="item-image"
                    :preview-src-list="item.imageUrls"
                    :initial-index="idx"
                    preview-teleported
                  />
                </el-carousel-item>
              </el-carousel>
              <el-image 
                v-else 
                :src="item.imageUrls[0]" 
                fit="cover" 
                class="item-image"
                :preview-src-list="item.imageUrls"
                preview-teleported
              />
            </div>
            
            <p class="item-description" v-if="item.description">{{ item.description }}</p>
            
            <div class="item-details">
              <div class="item-price" v-if="item.price">
                ¥{{ parseFloat(item.price).toFixed(2) }}
              </div>
              <div class="item-exchange" v-else-if="item.exchangeIntention">
                {{ item.exchangeIntention }}
              </div>
              
              <div class="item-meta">
                <el-tag size="small" type="info">{{ item.category }}</el-tag>
                <span class="item-time">{{ formatDate(item.createdAt) }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="6" animated />
      </div>
      
      <div v-if="hasMore" class="load-more-container">
        <el-button 
          :loading="loading" 
          @click="loadMore"
          size="large"
          round
        >
          {{ loading ? '加载中...' : '加载更多' }}
        </el-button>
      </div>
      
      <div v-if="!hasMore && items.length > 0" class="no-more">
        已加载全部商品
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: 'Market',
  data() {
    return {
      items: [],
      categories: [
        { label: '全部', value: '' },
        { label: '教材', value: '教材' },
        { label: '数码', value: '数码' },
        { label: '生活用品', value: '生活用品' },
        { label: '服装', value: '服装' },
        { label: '运动器材', value: '运动器材' },
        { label: '其他', value: '其他' }
      ],
      selectedCategory: '',
      loading: false,
      page: 0,
      size: 12,
      hasMore: true
    };
  },
  async mounted() {
    await this.fetchItems();
  },
  methods: {
    async fetchItems(reset = false) {
      if (this.loading) return;
      
      this.loading = true;
      
      try {
        const params = {
          page: reset ? 0 : this.page,
          size: this.size,
          sortBy: 'createdAt',
          sortDir: 'desc'
        };
        
        if (this.selectedCategory) {
          params.category = this.selectedCategory;
        }
        
        const response = await request.get('/items', { params });
        const newItems = response.items || [];
        
        if (reset) {
          this.items = newItems;
        } else {
          this.items = [...this.items, ...newItems];
        }
        
        this.hasMore = response.currentPage < response.totalPages - 1;
        if (!reset) {
          this.page++;
        }
      } catch (error) {
        console.error('获取商品列表失败:', error);
        this.$message.error('获取商品列表失败');
      } finally {
        this.loading = false;
      }
    },
    selectCategory(category) {
      if (this.selectedCategory === category) return;
      
      this.selectedCategory = category;
      this.page = 0;
      this.hasMore = true;
      this.fetchItems(true);
    },
    async loadMore() {
      await this.fetchItems();
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
.market-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.filter-section {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.category-tag {
  cursor: pointer;
  user-select: none;
}

.items-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.item-card-wrapper {
  break-inside: avoid;
}

.item-card {
  border-radius: 12px;
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-title {
  font-weight: bold;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 70%;
}

.status-badge {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
}

.on-shelf {
  background-color: #e6f7ee;
  color: #52c41a;
}

.sold-out {
  background-color: #f5f5f5;
  color: #8c8c8c;
}

.item-content {
  padding-top: 10px;
}

.item-images {
  margin-bottom: 10px;
}

.item-image {
  width: 100%;
  height: 150px;
  border-radius: 8px;
}

.item-description {
  color: #666;
  font-size: 14px;
  line-height: 1.5;
  margin: 10px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-details {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-top: 10px;
}

.item-price {
  font-size: 18px;
  font-weight: bold;
  color: #ff6b35;
}

.item-exchange {
  font-size: 14px;
  color: #1890ff;
}

.item-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
}

.item-time {
  color: #aaa;
  font-size: 12px;
}

.loading-container {
  grid-column: 1 / -1;
  padding: 20px;
}

.load-more-container {
  grid-column: 1 / -1;
  text-align: center;
  margin: 20px 0;
}

.no-more {
  grid-column: 1 / -1;
  text-align: center;
  color: #999;
  padding: 20px;
}

/* 瀑布流效果 */
@media (min-width: 768px) {
  .items-container {
    column-count: 2;
    column-gap: 20px;
  }
  
  .item-card-wrapper {
    break-inside: avoid;
    margin-bottom: 20px;
  }
}

@media (min-width: 1024px) {
  .items-container {
    column-count: 3;
  }
}
</style>