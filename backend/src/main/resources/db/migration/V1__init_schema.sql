-- FairCart Database Schema - Flyway Migration V1
-- Generated from JPA Entities
-- Version: 1.0.0
-- Description: Initial schema creation for FairCart E-Commerce Platform

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================
-- USERS TABLE
-- ============================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);

-- ============================================
-- USER VERIFICATION TABLE (OTP)
-- ============================================
CREATE TABLE user_verification (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    otp VARCHAR(10) NOT NULL,
    otp_type VARCHAR(10) NOT NULL CHECK (otp_type IN ('EMAIL', 'PHONE')),
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_verification_user ON user_verification(user_id);
CREATE INDEX idx_user_verification_otp ON user_verification(otp, otp_type, is_verified);

-- ============================================
-- CATEGORIES TABLE
-- ============================================
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INTEGER NOT NULL DEFAULT 0,
    parent_category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_categories_slug ON categories(slug);
CREATE INDEX idx_categories_active ON categories(active);
CREATE INDEX idx_categories_parent ON categories(parent_category_id);

-- ============================================
-- PLATFORMS TABLE
-- ============================================
CREATE TABLE platforms (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    code VARCHAR(50) NOT NULL UNIQUE,
    base_url VARCHAR(500),
    logo_url VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    scrapable BOOLEAN NOT NULL DEFAULT TRUE,
    config JSONB,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- SELLERS TABLE
-- ============================================
CREATE TABLE sellers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address TEXT,
    rating NUMERIC(3,2) NOT NULL DEFAULT 0.00,
    total_sales BIGINT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_sellers_email ON sellers(email);
CREATE INDEX idx_sellers_active ON sellers(active);

-- ============================================
-- PRODUCTS TABLE
-- ============================================
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price NUMERIC(12,2) NOT NULL,
    discount_price NUMERIC(12,2),
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE RESTRICT,
    seller_id BIGINT REFERENCES sellers(id) ON DELETE SET NULL,
    image_url VARCHAR(500),
    image_urls JSONB,
    intelligence_score INTEGER NOT NULL DEFAULT 0,
    rating_avg NUMERIC(3,2),
    total_reviews INTEGER NOT NULL DEFAULT 0,
    price_history_json JSONB,
    features_json JSONB,
    specification_json JSONB,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE', 'DISCONTINUED')),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_seller ON products(seller_id);
CREATE INDEX idx_products_status ON products(status);
CREATE INDEX idx_products_intelligence_score ON products(intelligence_score DESC);
CREATE INDEX idx_products_name ON products USING GIN (to_tsvector('english', name));
CREATE INDEX idx_products_price ON products(price);

-- ============================================
-- PRODUCT INTELLIGENCE TABLE
-- ============================================
CREATE TABLE product_intelligence (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL UNIQUE REFERENCES products(id) ON DELETE CASCADE,
    price_score INTEGER NOT NULL DEFAULT 0,
    rating_score INTEGER NOT NULL DEFAULT 0,
    seller_score INTEGER NOT NULL DEFAULT 0,
    availability_score INTEGER NOT NULL DEFAULT 0,
    value_score INTEGER NOT NULL DEFAULT 0,
    calculated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- WISHLIST TABLE
-- ============================================
CREATE TABLE wishlist (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, product_id)
);

CREATE INDEX idx_wishlist_user ON wishlist(user_id);
CREATE INDEX idx_wishlist_product ON wishlist(product_id);

-- ============================================
-- REVIEWS TABLE
-- ============================================
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    helpful_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, product_id)
);

CREATE INDEX idx_reviews_product ON reviews(product_id);
CREATE INDEX idx_reviews_user ON reviews(user_id);
CREATE INDEX idx_reviews_rating ON reviews(rating);

-- ============================================
-- PRICE HISTORY TABLE
-- ============================================
CREATE TABLE price_history (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    old_price NUMERIC(12,2) NOT NULL,
    new_price NUMERIC(12,2) NOT NULL,
    changed_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_price_history_product ON price_history(product_id);
CREATE INDEX idx_price_history_changed_at ON price_history(changed_at DESC);
CREATE INDEX idx_price_history_product_changed ON price_history(product_id, changed_at DESC);

-- ============================================
-- REVIEW SENTIMENT TABLE
-- ============================================
CREATE TABLE review_sentiments (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    platform VARCHAR(50) NOT NULL,
    total_reviews_analyzed INTEGER NOT NULL DEFAULT 0,
    genuine_reviews_count INTEGER NOT NULL DEFAULT 0,
    fake_reviews_detected INTEGER NOT NULL DEFAULT 0,
    overall_sentiment_score NUMERIC(5,2),
    positive_keywords JSONB,
    negative_keywords JSONB,
    top_pros JSONB,
    top_cons JSONB,
    seller_reliability_index NUMERIC(5,2),
    analysis_version INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_review_sentiments_product ON review_sentiments(product_id);
CREATE INDEX idx_review_sentiments_platform ON review_sentiments(platform);

-- ============================================
-- TRACKED PRODUCTS TABLE (Price Drop Alerts)
-- ============================================
CREATE TABLE tracked_products (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    platform VARCHAR(50) NOT NULL,
    target_price NUMERIC(12,2) NOT NULL,
    current_price NUMERIC(12,2),
    alert_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    last_notified_at TIMESTAMP WITHOUT TIME ZONE,
    notification_channel VARCHAR(20) NOT NULL DEFAULT 'EMAIL',
    last_checked_at TIMESTAMP WITHOUT TIME ZONE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tracked_products_user ON tracked_products(user_id);
CREATE INDEX idx_tracked_products_product ON tracked_products(product_id);
CREATE INDEX idx_tracked_products_alert ON tracked_products(alert_enabled, target_price);

-- ============================================
-- RECOMMENDATION LOGS TABLE
-- ============================================
CREATE TABLE recommendation_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    recommendation_type VARCHAR(30) NOT NULL CHECK (recommendation_type IN ('STRICT_BUDGET', 'SMART_STRETCH', 'AI_QUERY', 'PRICE_DROP_ALERT', 'SIMILAR_PRODUCT')),
    query_text TEXT,
    verdict_score INTEGER,
    verdict_label VARCHAR(20),
    stretch_budget_suggested NUMERIC(12,2),
    stretch_product_id BIGINT REFERENCES products(id) ON DELETE SET NULL,
    reasoning TEXT,
    ai_model_used VARCHAR(100),
    confidence_score NUMERIC(5,2),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_recommendation_logs_user ON recommendation_logs(user_id);
CREATE INDEX idx_recommendation_logs_product ON recommendation_logs(product_id);
CREATE INDEX idx_recommendation_logs_type ON recommendation_logs(recommendation_type);
CREATE INDEX idx_recommendation_logs_created ON recommendation_logs(created_at DESC);

-- ============================================
-- CHAT MESSAGES TABLE
-- ============================================
CREATE TABLE chat_messages (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'ASSISTANT', 'SYSTEM')),
    content TEXT NOT NULL,
    ai_model_used VARCHAR(100),
    tokens_used INTEGER,
    response_time_ms BIGINT,
    metadata JSONB,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_chat_messages_user ON chat_messages(user_id);
CREATE INDEX idx_chat_messages_created ON chat_messages(created_at ASC);

-- ============================================
-- AGGREGATED PRODUCTS TABLE (Cross-platform)
-- ============================================
CREATE TABLE aggregated_products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(500) NOT NULL,
    description TEXT,
    external_id VARCHAR(100) NOT NULL UNIQUE,
    platform_id BIGINT NOT NULL REFERENCES platforms(id) ON DELETE RESTRICT,
    category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    current_price NUMERIC(12,2),
    original_price NUMERIC(12,2),
    discount_percentage NUMERIC(5,2),
    effective_price NUMERIC(12,2),
    coupon_discount NUMERIC(12,2),
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    product_url VARCHAR(1000),
    image_url VARCHAR(1000),
    image_urls JSONB,
    rating_average NUMERIC(3,2),
    review_count INTEGER NOT NULL DEFAULT 0,
    seller_name VARCHAR(200),
    seller_rating NUMERIC(3,2),
    in_stock BOOLEAN NOT NULL DEFAULT TRUE,
    delivery_estimate VARCHAR(100),
    specifications JSONB,
    features_json JSONB,
    last_synced_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_agg_product_platform ON aggregated_products(platform_id);
CREATE INDEX idx_agg_product_category ON aggregated_products(category_id);
CREATE INDEX idx_agg_product_external_id ON aggregated_products(external_id);
CREATE INDEX idx_agg_product_price ON aggregated_products(current_price);
CREATE INDEX idx_agg_product_rating ON aggregated_products(rating_average DESC);
CREATE INDEX idx_agg_product_synced ON aggregated_products(last_synced_at DESC);

-- ============================================
-- PRODUCT PLATFORM LISTINGS TABLE
-- ============================================
CREATE TABLE product_platform_listings (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    platform VARCHAR(50) NOT NULL,
    external_id VARCHAR(100),
    platform_product_url VARCHAR(500),
    original_price NUMERIC(12,2),
    current_price NUMERIC(12,2),
    effective_price NUMERIC(12,2),
    discount_percentage NUMERIC(5,2),
    seller_name VARCHAR(200),
    seller_rating NUMERIC(3,2),
    delivery_estimate VARCHAR(100),
    in_stock BOOLEAN NOT NULL DEFAULT TRUE,
    rating_average NUMERIC(3,2),
    review_count INTEGER NOT NULL DEFAULT 0,
    last_synced_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ppl_product ON product_platform_listings(product_id);
CREATE INDEX idx_ppl_platform ON product_platform_listings(platform);
CREATE INDEX idx_ppl_external ON product_platform_listings(external_id);

-- ============================================
-- INITIAL DATA INSERTS
-- ============================================

-- Insert default categories
INSERT INTO categories (name, slug, description, image_url, display_order, active) VALUES
('Electronics', 'electronics', 'Electronic devices and gadgets', 'https://via.placeholder.com/300x200?text=Electronics', 1, TRUE),
('Smartphones', 'smartphones', 'Mobile phones and accessories', 'https://via.placeholder.com/300x200?text=Smartphones', 2, TRUE),
('Laptops', 'laptops', 'Laptops and notebooks', 'https://via.placeholder.com/300x200?text=Laptops', 3, TRUE),
('Audio', 'audio', 'Headphones, speakers, and audio equipment', 'https://via.placeholder.com/300x200?text=Audio', 4, TRUE),
('Wearables', 'wearables', 'Smartwatches and fitness trackers', 'https://via.placeholder.com/300x200?text=Wearables', 5, TRUE),
('Gaming', 'gaming', 'Gaming consoles and accessories', 'https://via.placeholder.com/300x200?text=Gaming', 6, TRUE),
('Home & Kitchen', 'home-kitchen', 'Home appliances and kitchen gadgets', 'https://via.placeholder.com/300x200?text=Home', 7, TRUE),
('Sports & Outdoors', 'sports-outdoors', 'Sports equipment and outdoor gear', 'https://via.placeholder.com/300x200?text=Sports', 8, TRUE);

-- Insert default platforms
INSERT INTO platforms (name, code, base_url, logo_url, active, scrapable) VALUES
('Amazon', 'AMAZON', 'https://www.amazon.com', 'https://via.placeholder.com/100x50?text=Amazon', TRUE, TRUE),
('Flipkart', 'FLIPKART', 'https://www.flipkart.com', 'https://via.placeholder.com/100x50?text=Flipkart', TRUE, TRUE),
('Tata Neu', 'TATA_NEU', 'https://www.tataneu.com', 'https://via.placeholder.com/100x50?text=Tata+Neu', TRUE, TRUE),
('Myntra', 'MYNTRA', 'https://www.myntra.com', 'https://via.placeholder.com/100x50?text=Myntra', TRUE, TRUE),
('Croma', 'CROMA', 'https://www.croma.com', 'https://via.placeholder.com/100x50?text=Croma', TRUE, TRUE),
('Reliance Digital', 'RELIANCE_DIGITAL', 'https://www.reliancedigital.in', 'https://via.placeholder.com/100x50?text=Reliance', TRUE, TRUE),
('AJIO', 'AJIO', 'https://www.ajio.com', 'https://via.placeholder.com/100x50?text=AJIO', TRUE, TRUE),
('Nykaa', 'NYKAA', 'https://www.nykaa.com', 'https://via.placeholder.com/100x50?text=Nykaa', TRUE, TRUE);

-- Create updated_at trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for updated_at on all tables
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_user_verification_updated_at BEFORE UPDATE ON user_verification FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_categories_updated_at BEFORE UPDATE ON categories FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_platforms_updated_at BEFORE UPDATE ON platforms FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_sellers_updated_at BEFORE UPDATE ON sellers FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_products_updated_at BEFORE UPDATE ON products FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_product_intelligence_updated_at BEFORE UPDATE ON product_intelligence FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_wishlist_updated_at BEFORE UPDATE ON wishlist FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_reviews_updated_at BEFORE UPDATE ON reviews FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_price_history_updated_at BEFORE UPDATE ON price_history FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_review_sentiments_updated_at BEFORE UPDATE ON review_sentiments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_tracked_products_updated_at BEFORE UPDATE ON tracked_products FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_recommendation_logs_updated_at BEFORE UPDATE ON recommendation_logs FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_chat_messages_updated_at BEFORE UPDATE ON chat_messages FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_aggregated_products_updated_at BEFORE UPDATE ON aggregated_products FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_product_platform_listings_updated_at BEFORE UPDATE ON product_platform_listings FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- VIEWS FOR COMMON QUERIES
-- ============================================

-- View for product with intelligence score and category
CREATE OR REPLACE VIEW v_product_with_details AS
SELECT 
    p.id,
    p.name,
    p.description,
    p.price,
    p.discount_price,
    p.stock_quantity,
    p.image_url,
    p.intelligence_score,
    p.rating_avg,
    p.total_reviews,
    p.status,
    p.created_at,
    p.updated_at,
    c.id AS category_id,
    c.name AS category_name,
    c.slug AS category_slug,
    s.id AS seller_id,
    s.name AS seller_name,
    s.rating AS seller_rating
FROM products p
LEFT JOIN categories c ON p.category_id = c.id
LEFT JOIN sellers s ON p.seller_id = s.id
WHERE p.status = 'ACTIVE';

-- View for user wishlist with product details
CREATE OR REPLACE VIEW v_user_wishlist AS
SELECT 
    w.id AS wishlist_id,
    w.user_id,
    w.product_id,
    w.created_at AS added_at,
    p.name,
    p.price,
    p.image_url,
    p.intelligence_score,
    p.rating_avg,
    p.stock_quantity,
    c.name AS category_name
FROM wishlist w
JOIN products p ON w.product_id = p.id
LEFT JOIN categories c ON p.category_id = c.id
WHERE p.status = 'ACTIVE';

-- View for product price statistics
CREATE OR REPLACE VIEW v_product_price_stats AS
SELECT 
    ph.product_id,
    MIN(ph.new_price) AS lowest_price,
    MAX(ph.new_price) AS highest_price,
    AVG(ph.new_price) AS average_price,
    COUNT(*) AS total_changes,
    MAX(ph.changed_at) AS last_change
FROM price_history ph
GROUP BY ph.product_id;

-- ============================================
-- FUNCTIONS
-- ============================================

-- Function to calculate intelligence score
CREATE OR REPLACE FUNCTION calculate_intelligence_score(product_id BIGINT)
RETURNS INTEGER AS $$
DECLARE
    v_product RECORD;
    v_price_score INTEGER := 0;
    v_rating_score INTEGER := 0;
    v_seller_score INTEGER := 0;
    v_availability_score INTEGER := 0;
    v_total_score INTEGER := 0;
BEGIN
    SELECT * INTO v_product FROM products WHERE id = product_id;
    
    IF NOT FOUND THEN
        RETURN 0;
    END IF;
    
    -- Price Score (30%)
    -- Compare against category average
    v_price_score := 20; -- Placeholder
    
    -- Rating Score (25%)
    v_rating_score := COALESCE(v_product.rating_avg * 20, 25);
    
    -- Seller Score (25%)
    v_seller_score := 20; -- Placeholder
    
    -- Availability Score (20%)
    IF v_product.stock_quantity >= 100 THEN
        v_availability_score := 20;
    ELSIF v_product.stock_quantity >= 50 THEN
        v_availability_score := 16;
    ELSIF v_product.stock_quantity >= 20 THEN
        v_availability_score := 12;
    ELSIF v_product.stock_quantity >= 10 THEN
        v_availability_score := 8;
    ELSIF v_product.stock_quantity >= 5 THEN
        v_availability_score := 4;
    ELSIF v_product.stock_quantity >= 1 THEN
        v_availability_score := 2;
    ELSE
        v_availability_score := 0;
    END IF;
    
    v_total_score := v_price_score + v_rating_score + v_seller_score + v_availability_score;
    
    RETURN LEAST(100, GREATEST(0, v_total_score));
END;
$$ LANGUAGE plpgsql;

-- Function to update product intelligence score
CREATE OR REPLACE FUNCTION update_product_intelligence()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
        INSERT INTO product_intelligence (product_id, price_score, rating_score, seller_score, availability_score, value_score, calculated_at)
        VALUES (NEW.id, 
                20, -- placeholder price_score
                COALESCE(NEW.rating_avg * 20, 25),
                20, -- placeholder seller_score
                CASE 
                    WHEN NEW.stock_quantity >= 100 THEN 20
                    WHEN NEW.stock_quantity >= 50 THEN 16
                    WHEN NEW.stock_quantity >= 20 THEN 12
                    WHEN NEW.stock_quantity >= 10 THEN 8
                    WHEN NEW.stock_quantity >= 5 THEN 4
                    WHEN NEW.stock_quantity >= 1 THEN 2
                    ELSE 0
                END,
                0, -- value_score calculated separately
                CURRENT_TIMESTAMP)
        ON CONFLICT (product_id) DO UPDATE SET
            price_score = 20,
            rating_score = COALESCE(NEW.rating_avg * 20, 25),
            seller_score = 20,
            availability_score = CASE 
                WHEN NEW.stock_quantity >= 100 THEN 20
                WHEN NEW.stock_quantity >= 50 THEN 16
                WHEN NEW.stock_quantity >= 20 THEN 12
                WHEN NEW.stock_quantity >= 10 THEN 8
                WHEN NEW.stock_quantity >= 5 THEN 4
                WHEN NEW.stock_quantity >= 1 THEN 2
                ELSE 0
            END,
            value_score = 0,
            calculated_at = CURRENT_TIMESTAMP;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for product intelligence updates
CREATE TRIGGER trigger_update_product_intelligence
AFTER INSERT OR UPDATE ON products
FOR EACH ROW EXECUTE FUNCTION update_product_intelligence();

-- ============================================
-- GRANT PERMISSIONS (adjust as needed for your roles)
-- ============================================
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO faircart_app;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO faircart_app;
-- GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO faircart_app;

-- ============================================
-- COMMENTS
-- ============================================
COMMENT ON TABLE users IS 'User accounts with authentication details';
COMMENT ON TABLE user_verification IS 'OTP verification tokens for email/phone';
COMMENT ON TABLE categories IS 'Product categories with hierarchy support';
COMMENT ON TABLE platforms IS 'E-commerce platforms for aggregation';
COMMENT ON TABLE sellers IS 'Third-party sellers on platforms';
COMMENT ON TABLE products IS 'Core product catalog';
COMMENT ON TABLE product_intelligence IS 'AI-calculated intelligence scores';
COMMENT ON TABLE wishlist IS 'User wishlist items';
COMMENT ON TABLE reviews IS 'User product reviews and ratings';
COMMENT ON TABLE price_history IS 'Historical price tracking';
COMMENT ON TABLE review_sentiments IS 'AI-analyzed review sentiment data';
COMMENT ON TABLE tracked_products IS 'User price drop alerts';
COMMENT ON TABLE recommendation_logs IS 'AI recommendation history';
COMMENT ON TABLE chat_messages IS 'AI chat conversation history';
COMMENT ON TABLE aggregated_products IS 'Cross-platform product listings';
COMMENT ON TABLE product_platform_listings IS 'Platform-specific product prices';

COMMENT ON FUNCTION calculate_intelligence_score IS 'Calculates the AI intelligence score for a product';
COMMENT ON FUNCTION update_product_intelligence IS 'Trigger function to update product intelligence scores';

-- End of V1 Migration