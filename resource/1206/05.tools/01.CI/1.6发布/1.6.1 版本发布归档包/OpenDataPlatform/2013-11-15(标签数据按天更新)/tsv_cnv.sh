for file in $fdPath/*
do
     echo "processing $file"
     ./tsv_stdcnv_each.sh $file tsv.col_name
done