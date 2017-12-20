#!/bin/bash

home=`dirname $0`
home="../"$home
echo $home

target=$home/wordpress/wp-content/plugins/woocommerce/includes/export/class-wc-product-csv-exporter.php

ls $target

grep "=> __" $target | awk -F "'" '{print "\""$2"\":" " " "\""$4"\""}'
