#!/bin/bash
./match-manager convert ../test-data/cw/c.txt ../test-data/cw/c.xml -config=../conf/s-match-Tab2XML.xml
./match-manager convert ../test-data/cw/w.txt ../test-data/cw/w.xml -config=../conf/s-match-Tab2XML.xml

./match-manager offline ../test-data/cw/c.xml ../test-data/cw/c.xml
./match-manager offline ../test-data/cw/w.xml ../test-data/cw/w.xml

./match-manager online ../test-data/cw/c.xml ../test-data/cw/w.xml ../test-data/cw/result-cw.txt
./match-manager online ../test-data/cw/c.xml ../test-data/cw/w.xml ../test-data/cw/result-minimal-cw.txt -config=../conf/s-match-minimal.xml
