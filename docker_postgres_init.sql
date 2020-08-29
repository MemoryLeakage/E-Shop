CREATE USER eshopapp WITH PASSWORD 'P@ssw0rd' CREATEDB;
CREATE DATABASE eshop
    WITH
    OWNER = eshopapp
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;