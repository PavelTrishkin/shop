-- USERS
CREATE TABLE user_seq (next_val bigint);
INSERT INTO user_seq VALUES ( 1 );
CREATE TABLE users (
    id bigint not null,
    archive bit not null,
    email varchar(255),
    name varchar(255),
    password varchar(255),
    role varchar(255),
    bucket_id bigint,
    primary key (id)
);

-- BUCKET
CREATE TABLE bucket_seq (next_val bigint);
INSERT INTO bucket_seq VALUES ( 1 );
CREATE TABLE buckets (
    id bigint not null,
    user_id bigint,
    primary key (id)
);

-- LINK BETWEEN BUCKET AND USER
ALTER TABLE buckets
    ADD CONSTRAINT foreign key (user_id)
    REFERENCES users (id);
ALTER TABLE users
    ADD CONSTRAINT foreign key (bucket_id)
    REFERENCES buckets (id);

-- CATEGORY
CREATE TABLE category_seq (next_val bigint);
INSERT INTO category_seq VALUES ( 1 );
CREATE TABLE categories (
    id bigint not null,
    title varchar(255),
    primary key (id)
);

-- PRODUCTS
CREATE TABLE product_seq (next_val bigint);
INSERT INTO product_seq VALUES ( 1 );
CREATE TABLE products (
    id bigint not null,
    price double precision,
    title varchar(255),
    primary key (id)
);

-- CATEGORY AND PRODUCT
CREATE TABLE products_categories (
    product_id bigint not null,
    category_id bigint not null
);
ALTER TABLE products_categories
    ADD CONSTRAINT foreign key (category_id)
    REFERENCES categories (id);
ALTER TABLE products_categories
    ADD CONSTRAINT foreign key (product_id)
    REFERENCES products (id);

-- PRODUCTS IN BUCKET
CREATE TABLE bucket_products (
    bucket_id bigint not null,
    product_id bigint not null
);
ALTER TABLE bucket_products
    ADD CONSTRAINT foreign key (product_id)
    REFERENCES products (id);
ALTER TABLE bucket_products
    ADD CONSTRAINT foreign key (bucket_id)
    REFERENCES buckets (id);

-- ORDERS
CREATE TABLE order_seq (next_val bigint);
INSERT INTO order_seq VALUES ( 1 );
CREATE TABLE orders (
    id bigint not null,
    address varchar(255),
    created datetime NULL,
    updated datetime NULL,
    order_status varchar(255),
    sum decimal(19,2),
    user_id bigint,
    primary key (id)
);

ALTER TABLE orders
    ADD CONSTRAINT foreign key (user_id)
    REFERENCES users (id);

-- DETAILS OF ORDER
CREATE TABLE order_details_seq (next_val bigint);
INSERT INTO order_details_seq VALUES ( 1 );
CREATE TABLE order_details (
    id bigint not null,
    amount decimal(19,2),
    price double precision,
    order_id bigint,
    product_id bigint,
    primary key (id)
);
CREATE TABLE orders_order_details (
    order_id bigint not null,
    order_details_id bigint not null
);
ALTER TABLE orders_order_details
    ADD CONSTRAINT UNIQUE (order_details_id);
ALTER TABLE order_details
    ADD CONSTRAINT foreign key (order_id)
    REFERENCES orders (id);
ALTER TABLE order_details
    ADD CONSTRAINT foreign key (product_id)
    REFERENCES products (id);