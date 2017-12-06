﻿#!/bin/sh

read -p "Please enter the root's password of mysql:" -t 30 password

echo "Creating database..."

mysqlcmd="mysql -u root -p"$password
$mysqlcmd -e "source tccserverdb_creat_database.sql;source tccserverdb_creat_user.sql;SET GLOBAL log_bin_trust_function_creators=1;"
mysqlcmd="mysql -u tccserverdb -pxd7!F-nj tccserverdb-NJ"
$mysqlcmd -e "source tccserverdb_creat_table.sql;tccserverdb_creat_procedure.sql;tccserverdb_init_data.sql;"
mysqlcmd="mysql -u root -p"$password
$mysqlcmd -e "SET GLOBAL log_bin_trust_function_creators=0;"
echo "Database created success!"