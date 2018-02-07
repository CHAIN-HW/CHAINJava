call match-manager.bat convert ..\test-data\spsm\source.txt ..\test-data\spsm\source.xml -config=..\conf\s-match-Function2XML.xml
call match-manager.bat convert ..\test-data\spsm\target.txt ..\test-data\spsm\target.xml -config=..\conf\s-match-Function2XML.xml

call match-manager.bat offline ..\test-data\spsm\source.xml ..\test-data\spsm\source.xml
call match-manager.bat offline ..\test-data\spsm\target.xml ..\test-data\spsm\target.xml

call match-manager.bat online ..\test-data\spsm\source.xml ..\test-data\spsm\target.xml ..\test-data\spsm\result-smatch-only.txt

call match-manager.bat online ..\test-data\spsm\source.xml ..\test-data\spsm\target.xml ..\test-data\spsm\result-spsm.txt -config=..\conf\s-match-spsm.xml