
INSERT INTO user_model (created_at, login, email, password) VALUES ('login', 'email', 'password', 'created_at')


INSERT INTO favourite (owner_id) VALUES (1)
INSERT INTO image (name, bytes) VALUES ('name', null)

INSERT INTO city (name) VALUES ('Warszawa')
INSERT INTO city (name) VALUES ('Kielce')
INSERT INTO city (name) VALUES ('KRAKOW')
INSERT INTO brand (name) VALUES ('BMW')
INSERT INTO brand (name) VALUES ('AUDI')



INSERT INTO announcement(type, price, brand_id, owner_id, city_id, year, first_owner, damaged ) VALUES ('Auto', 12000, 1, 1, 1, 1999, true, true)
INSERT INTO announcement(type, price, brand_id, owner_id, city_id, year, first_owner, damaged ) VALUES ('Auto', 50000, 1, 1, 3, 2020, true, true)
INSERT INTO announcement(type, price, brand_id, owner_id, city_id, year, first_owner, damaged ) VALUES ('Auto', 40000, 2, 1, 2, 1980, true, true)
INSERT INTO announcement(type, price, brand_id, owner_id, city_id, year, first_owner, damaged ) VALUES ('Auto', 100000, 1, 1, 1, 2021, true, true)
INSERT INTO announcement(type, price, brand_id, owner_id, city_id, year, first_owner, damaged ) VALUES ('Auto', 35000, 2, 1, 1, 1994, true, true)
INSERT INTO announcement(type, price, brand_id, owner_id, city_id, year, first_owner, damaged ) VALUES ('Auto', 1000, 1, 1, 1, 1991, true, true)
INSERT INTO announcement(type, price, brand_id, owner_id, city_id, year, first_owner, damaged ) VALUES ('Auto', 45000, 1, 1, 2, 1991, true, true)
INSERT INTO announcement(type, price, brand_id, owner_id, city_id, year, first_owner, damaged ) VALUES ('Auto', 45000, 1, 1, 2, 1991, true, false)

