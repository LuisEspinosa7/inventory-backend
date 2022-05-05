-- USERS AND ROLES PERMISSIONS

\c postgres postgres
CREATE USER inventoryadmin WITH PASSWORD '123456';
CREATE DATABASE inventory WITH OWNER inventoryadmin;
\c inventory postgres
DROP SCHEMA public;

CREATE SCHEMA app AUTHORIZATION inventoryadmin;
CREATE ROLE appusage;

\c inventory inventoryadmin;

GRANT CONNECT ON DATABASE inventory TO appusage;
GRANT USAGE ON SCHEMA app TO appusage;


ALTER DEFAULT PRIVILEGES FOR ROLE inventoryadmin
   REVOKE EXECUTE ON FUNCTIONS FROM PUBLIC;

ALTER DEFAULT PRIVILEGES FOR ROLE inventoryadmin IN SCHEMA app
   GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO appusage;
ALTER DEFAULT PRIVILEGES FOR ROLE inventoryadmin IN SCHEMA app
   GRANT SELECT, USAGE ON SEQUENCES TO appusage;
ALTER DEFAULT PRIVILEGES FOR ROLE inventoryadmin IN SCHEMA app
   GRANT EXECUTE ON FUNCTIONS TO appusage;

REVOKE ALL ON TABLE app.audit_log FROM appusage;

\c inventory postgres

CREATE USER inventoryapp WITH PASSWORD '123456';
GRANT appusage TO inventoryapp;
