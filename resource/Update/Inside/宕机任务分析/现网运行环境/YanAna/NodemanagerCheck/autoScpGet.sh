#!/usr/bin/expect -f
cd ./files
spawn scp c00239107@10.41.242.227:/home/c00239107/nodefile/* ./
expect "Password:"
send "Android9(\r"
interact


