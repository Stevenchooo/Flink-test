echo "*** converting $1"
echo "** the col_name file=$2"
echo "*** lines in $1"=`wc -l $1`

echo "*** copying headers to the result file"
cp $2 $1.cnv

echo "*** replacing delimiter CTRL-A with comma"
sed -e 's/\x01/,/g' -e 's/\x03/;/g' $1 >> $1.cnv
echo "*** replaced delimiter CTRL-A with comma"

#echo "*** lines in hive.exp.cnv"=`wc -l hive.exp.cnv`
echo "*** check the last 3 lines of the replaced file"
head -3 $1.cnv

echo "!!! converted $1"
