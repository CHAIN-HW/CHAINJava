#!/bin/bash
./match-manager convert ../../../inputs/source.txt ../../../inputs/source.xml -config=../conf/s-match-Function2XML.xml
./match-manager convert ../../../inputs/target.txt ../../../inputs/target.xml -config=../conf/s-match-Function2XML.xml

./match-manager offline ../../../inputs/source.xml ../../../inputs/source.xml
./match-manager offline ../../../inputs/target.xml ../../../inputs/target.xml

./match-manager online ../../../inputs/source.xml ../../../inputs/target.xml ../../../outputs/result-spsm.txt -config=../conf/s-match-spsm-prolog.xml
