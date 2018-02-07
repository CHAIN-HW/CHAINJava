#!/bin/bash
./match-manager convert ../test-data/spsm/source.txt ../test-data/spsm/source.xml -config=../conf/s-match-Function2XML.xml
./match-manager convert ../test-data/spsm/target.txt ../test-data/spsm/target.xml -config=../conf/s-match-Function2XML.xml

./match-manager offline ../test-data/spsm/source.xml ../test-data/spsm/source.xml
./match-manager offline ../test-data/spsm/target.xml ../test-data/spsm/target.xml

./match-manager online ../test-data/spsm/source.xml ../test-data/spsm/target.xml ../test-data/spsm/result-smatch-only.txt

./match-manager online ../test-data/spsm/source.xml ../test-data/spsm/target.xml ../test-data/spsm/result-spsm.txt -config=../conf/s-match-spsm.xml
