#!/bin/sh

read -p "Please enter the root's password of mysql:" -t 30 password

echo "Creating database..."

mysqlcmd="mysql -u root -p"$password
$mysqlcmd -e "source odpdatadb_create_database.sql;source odpdatadb_create_user.sql;SET GLOBAL log_bin_trust_function_creators=1;"
mysqlcmd="mysql -u odpdatadb -pXXXXXX odpdatadb"
$mysqlcmd -e "source odpdatadb_create_table.sql;source odpdatadb_create_procedure.sql;source odpdatadb_init_data.sql;"
mysqlcmd="mysql -u root -p"$password
$mysqlcmd -e "SET GLOBAL log_bin_trust_function_creators=0;"
echo "Database created success!"