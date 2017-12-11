MFS目录:/MFS/DataIn/Persona/odsdata/ods_persona_red_list_dm
解密脚本:java -jar /MFS/DataIn/Persona/odsdata/ods_persona_red_list_dm/AesFileDecryptor.jar /MFS/DataIn/Persona/odsdata/ods_persona_red_list_dm/PushCMS_RedList_2017050200.txt /MFS/DataIn/Persona/odsdata/ods_persona_red_list_dm/PushCMS_RedList_2017050200.log

#!/bin/bash
RECYLE=${1:0:8}
JAVA_PATH=/MFS/Dataone/BICommon/FIClient/JDK/jdk/bin/java
FILE_PATH=/MFS/DataIn/Persona/odsdata/ods_persona_red_list_dm
INPUT_FILE=PushCMS_RedList_$RECYLE00.txt
OUTPUT_FILE=PushCMS_RedList_$RECYLE00.log

$JAVA_PATH -jar $FILE_PATH/$INPUT_FILE $FILE_PATH/$OUTPUT_FILE

--检查解密文件是否存在
if [ $? -eq 0 ]
then
    echo "load data succeed......"
else
    echo "load data failed......"
    exit 1
fi
