#!/bin/sh

read -p "Please enter the root's password of mysql:" -t 30 password

echo "Creating database..."

mysqlcmd="mysql -u root -p"$password
$mysqlcmd -e "source tccserverdb_creat_database.sql;source tccserverdb_creat_user.sql;"
mysqlcmd="mysql -u tccserverdb -ptccserverdb tccserverdb"
$mysqlcmd -e "SET GLOBAL log_bin_trust_function_creators=1;source tccserverdb_creat_table.sql;tccserverdb_creat_procedure.sql;SET GLOBAL log_bin_trust_function_creators=0;tccserverdb_init_data.sql;"

echo "Database created success!"