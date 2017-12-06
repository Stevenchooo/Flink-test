echo "started"
date
for file in hive.exp_alliance1dot5/*.cnv
do
     echo "processing $file"
     date
     $FASTBIT_HOME/examples/ardea -d tsv_index_alliance1dot5 -M "tsv_roman.col_types_1dot5" -t $file
     date
     echo "processed $file"
done
date
echo "ended"
