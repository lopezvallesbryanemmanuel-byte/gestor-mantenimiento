IF DB_ID('mantenimiento_db') IS NULL
    CREATE DATABASE mantenimiento_db;
GO

IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = 'app_mant')
    CREATE LOGIN app_mant WITH PASSWORD = '$(APP_DB_PASSWORD)';
GO

USE mantenimiento_db;
GO

IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'app_mant')
BEGIN
    CREATE USER app_mant FOR LOGIN app_mant;
    ALTER ROLE db_datareader ADD MEMBER app_mant;  -- puede leer
    ALTER ROLE db_datawriter ADD MEMBER app_mant;  -- puede insertar, actualizar y borrar
END
GO