#/bin/sh
source /opt/Kafka_Client/bigdata_env
cd /home/kafka2es/testChenzhiliang
# 格式1=cmd1 topic parition数量    key/value/all/all_save     key或value的标志
# 格式2=cmd2 topic key_from_partition_offset1_to_offset2 0(partition) 1(offset begin) 2(offsetend)  
java -cp .:./lib/*:./conf com.huawei.bigdata.kafka.example.SimpleConsumerDemo cmd1 topic_source 100 all_save  8db29962d60d.zip
#java -cp .:./lib/*:./conf com.huawei.bigdata.kafka.example.SimpleConsumerDemo cmd2 topic_source key_from_partition_offset1_to_offset2 45 176413 176425  
