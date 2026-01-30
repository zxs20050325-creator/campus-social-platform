-- 用户表
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    student_id VARCHAR(20) UNIQUE NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    avatar_url TEXT,
    school VARCHAR(100),
    contact_info VARCHAR(100), -- 微信或其他联系方式
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 商品表（用于资源置换）
CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    image_urls TEXT[], -- 图片URL数组
    price DECIMAL(10, 2),
    exchange_intention VARCHAR(200), -- 交换意向
    category VARCHAR(50), -- 分类：教材/数码/生活等
    user_id INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'ON_SHELF', -- ON_SHELF/ SOLD_OUT 上架/已出
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 帖子表（用于校园社交）
CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    image_urls TEXT[], -- 图片URL数组
    user_id INTEGER NOT NULL,
    like_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 评论表（关联帖子或商品）
CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    user_id INTEGER NOT NULL,
    post_id INTEGER, -- 关联帖子ID，可为空
    item_id INTEGER, -- 关联商品ID，可为空
    parent_comment_id INTEGER, -- 回复评论ID，可为空（用于回复评论的场景）
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comments(id) ON DELETE CASCADE,
    CHECK ((post_id IS NOT NULL AND item_id IS NULL) OR (post_id IS NULL AND item_id IS NOT NULL))
);