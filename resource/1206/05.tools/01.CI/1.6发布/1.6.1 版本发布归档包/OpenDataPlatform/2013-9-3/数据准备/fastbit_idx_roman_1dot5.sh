
echo "building index is started"
date
$FASTBIT_HOME/examples/ibis -v -d "tsv_index_alliance1dot5" \
-b 32 -b "rowkey:none" \
-b 'game_interest:keywords delimiters=";"' \
-b 'lat_interest:keywords delimiters=";"' \
-b 'app_channel:keywords delimiters=";"' \
-b 'app_list:keywords delimiters=";"' \
-b 'defined_interest:keywords delimiters=";"' \
-b "*:<binning none/><encoding equality/>" -z \
>fb_idx.out 2>fb_idx.err

date
echo "building index is done"
