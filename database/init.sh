#!/bin/bash
set -e

SQLCMD=(/opt/mssql-tools18/bin/sqlcmd -S db -U sa -P "$MSSQL_SA_PASSWORD" -C -b -f 65001)

EXISTE=$("${SQLCMD[@]}" -h -1 -W -Q "SET NOCOUNT ON; SELECT COUNT(*) FROM sys.databases WHERE name = 'mantenimiento_db'")

if [ "$EXISTE" = "1" ]; then
  echo "La base de datos ya existe; no se vuelven a ejecutar los scripts."
  exit 0
fi

for archivo in /scripts/00_create_db.sql /scripts/01_schema.sql /scripts/02_data.sql; do
  echo "Ejecutando $archivo ..."
  "${SQLCMD[@]}" -v APP_DB_PASSWORD="$DB_PASSWORD" -i "$archivo"
done

echo "Base de datos lista."