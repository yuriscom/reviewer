#!/bin/bash

if [ -z $1 ] ; then
   echo use: $0 descriptive_name
   exit 1
fi

ds=`date -u +%Y%m%d%H%M%S`
sp="__"
migration_name=V$ds$sp$1

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
touch "$DIR/src/main/resources/db/migration/$migration_name.sql"