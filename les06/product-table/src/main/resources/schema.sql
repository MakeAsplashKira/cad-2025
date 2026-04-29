CREATE TABLE IF NOT EXISTS CATEGORIES (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500)
    );

CREATE TABLE IF NOT EXISTS PRODUCTS (
                                        product_id INT PRIMARY KEY AUTO_INCREMENT,
                                        name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    category_id INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id)
    );