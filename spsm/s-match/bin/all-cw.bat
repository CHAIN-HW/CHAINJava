call match-manager.bat convert ..\test-data\cw\c.txt ..\test-data\cw\c.xml -config=..\conf\s-match-Tab2XML.xml
call match-manager.bat convert ..\test-data\cw\w.txt ..\test-data\cw\w.xml -config=..\conf\s-match-Tab2XML.xml

call match-manager.bat offline ..\test-data\cw\c.xml ..\test-data\cw\c.xml
call match-manager.bat offline ..\test-data\cw\w.xml ..\test-data\cw\w.xml

call match-manager.bat online ..\test-data\cw\c.xml ..\test-data\cw\w.xml ..\test-data\cw\result-cw.txt
call match-manager.bat online ..\test-data\cw\c.xml ..\test-data\cw\w.xml ..\test-data\cw\result-minimal-cw.txt -config=..\conf\s-match-minimal.xml