echo "started"
date
for file in $fdPath/*.cnv
do
     echo "processing $file"
     date
     $FASTBIT_HOME/examples/ardea -d $idxPath -M "tsv.col_types" -t $file
     date
     echo "processed $file"
done
date
echo "ended"
