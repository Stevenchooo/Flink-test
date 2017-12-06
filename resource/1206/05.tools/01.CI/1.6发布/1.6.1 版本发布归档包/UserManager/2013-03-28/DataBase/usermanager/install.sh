#!/bin/sh

read -p "Please enter the root's password of mysql:" -t 30 password

echo "Creating database..."

mysqlcmd="mysql -u root -p"$password
$mysqlcmd -e "source umdb_creat_database.sql;source umdb_creat_user.sql;SET GLOBAL log_bin_trust_function_creators=1;"
mysqlcmd="mysql -u usermanager -pXXXXXX usermanager"
$mysqlcmd -e "source umdb_creat_table.sql;source umdb_creat_procedure.sql;source umdb_init_data.sql;"
mysqlcmd="mysql -u root -p"$password
$mysqlcmd -e "SET GLOBAL log_bin_trust_function_creators=0;"
echo "Database created success!"