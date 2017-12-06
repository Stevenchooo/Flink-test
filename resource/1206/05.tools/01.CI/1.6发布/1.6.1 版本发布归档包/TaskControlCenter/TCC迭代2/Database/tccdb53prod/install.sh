#!/bin/sh

read -p "Please enter the root's password of mysql:" -t 30 password

echo "Creating database..."

mysqlcmd="mysql -u root -p"$password
$mysqlcmd -e "source tccserverdb_create_database.sql;source tccserverdb_create_user.sql;SET GLOBAL log_bin_trust_function_creators=1;"
mysqlcmd="mysql -u tccdb53prod -pf3,sI84w tccdb53prod"
$mysqlcmd -e "source tccserverdb_create_table.sql;source tccserverdb_create_procedure.sql;source tccserverdb_init_data.sql;"
mysqlcmd="mysql -u root -p"$password
$mysqlcmd -e "SET GLOBAL log_bin_trust_function_creators=0;"
echo "Database created success!"