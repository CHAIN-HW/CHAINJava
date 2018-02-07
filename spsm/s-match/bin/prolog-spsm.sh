#!/bin/bash
./match-manager convert $1 $1.xml -config=../conf/s-match-Function2XML.xml
./match-manager convert $2 $2.xml -config=../conf/s-match-Function2XML.xml

./match-manager offline $1.xml $1.xml
./match-manager offline $2.xml $2.xml

./match-manager online $1.xml $2.xml $3 -config=../conf/s-match-spsm-prolog.xml
