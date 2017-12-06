#!/bin/sh
RESULT_FILE=./result.txt
BLOCK_SIZE=1024
cd `dirname $0`

DIRSINFO=`df -B $BLOCK_SIZE | awk '/data/ {print $6}'`
DISKAVIAL=(`df -B $BLOCK_SIZE | awk '/data/ {print $4}'`)
if [ -f $RESULT_FILE ]
then
  rm $RESULT_FILE
fi

i=0

VERSION_FILE=""
for DIR in $DIRSINFO
do
   if [ -f $DIR/hadoop/data/current/VERSION ]
   then
      VERSION_FILE=$DIR/hadoop/data/current/VERSION
      break
   fi
done

if [ "$VERSION_FILE" = "" ]
then
   echo "no any version file exists in disk"
   exit 1;
fi

for DIR in $DIRSINFO
do
   if [ ! -d $DIR/hadoop/data/current ]
   then
      echo "ignore disk[$DIR]."
      continue
   fi

   SUBDIRS=`du -s -B $BLOCK_SIZE $DIR/hadoop/data/current/subdir* | awk '{print $2}'`
   SIZES=(`du -s -B $BLOCK_SIZE $DIR/hadoop/data/current/subdir* | awk '{print $1}'`)
   AVIAL=${DISKAVIAL[$i]}
   if [ "$SUBDIRS" = "" ]
   then
      echo "cp $VERSION_FILE $DIR/hadoop/data/current/" 
      cp $VERSION_FILE $DIR/hadoop/data/current/
      echo "mkdir $DIR/hadoop/data/current/subdir0"
      mkdir $DIR/hadoop/data/current/subdir0
      SUBDIRS=`du -s -B $BLOCK_SIZE $DIR/hadoop/data/current/subdir* | awk '{print $2}'`
      SIZES=(`du -s -B $BLOCK_SIZE $DIR/hadoop/data/current/subdir* | awk '{print $1}'`)
   fi

   echo "list the hdfs data directories in disk[$DIR]!"
   j=0
   for subdir in $SUBDIRS
   do
     size=${SIZES[$j]}
     line=`expr $size \* $BLOCK_SIZE`,$subdir,`expr $AVIAL \* $BLOCK_SIZE`
     echo $line
     echo $line >> $RESULT_FILE
     j=`expr $j + 1`
   done
   i=`expr $i + 1`
done
exit 0
