for file in hive.exp_alliance1dot5/*
do
     echo "processing $file"
     ./tsv_stdcnv_each.sh $file tsv.col_name4_roman_1dot5
done