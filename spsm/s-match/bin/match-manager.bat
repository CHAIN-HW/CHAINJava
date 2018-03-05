@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup
set REPO=


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\lib

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\spring-context-4.1.9.RELEASE.jar;"%REPO%"\spring-aop-4.1.9.RELEASE.jar;"%REPO%"\aopalliance-1.0.jar;"%REPO%"\spring-beans-4.1.9.RELEASE.jar;"%REPO%"\spring-core-4.1.9.RELEASE.jar;"%REPO%"\commons-logging-1.2.jar;"%REPO%"\spring-expression-4.1.9.RELEASE.jar;"%REPO%"\icoreader-1.0.4.jar;"%REPO%"\icon-1.0.0.jar;"%REPO%"\svgsalamander-1.0.0.jar;"%REPO%"\org.semanticweb.hermit-1.3.1.jar;"%REPO%"\minisat-1.0.0.jar;"%REPO%"\orbital-core-1.3.0.jar;"%REPO%"\orbital-ext-1.3.0.jar;"%REPO%"\owlapi-3.2.4.jar;"%REPO%"\skosapi-1.0.0.jar;"%REPO%"\jgoodies-forms-1.7.2.jar;"%REPO%"\jgoodies-common-1.7.0.jar;"%REPO%"\s-match-core-2.0.0-SNAPSHOT.jar;"%REPO%"\jackson-annotations-2.8.5.jar;"%REPO%"\s-match-wordnet-2.0.0-SNAPSHOT.jar;"%REPO%"\extjwnl-1.8.0.jar;"%REPO%"\concurrentlinkedhashmap-lru-1.3.2.jar;"%REPO%"\extjwnl-data-wn31-1.2.jar;"%REPO%"\s-match-io-2.0.0-SNAPSHOT.jar;"%REPO%"\xercesImpl-2.11.0.jar;"%REPO%"\xml-apis-1.4.01.jar;"%REPO%"\s-match-logic-2.0.0-SNAPSHOT.jar;"%REPO%"\org.ow2.sat4j.core-2.3.5.jar;"%REPO%"\aima-core-0.11.0.jar;"%REPO%"\s-match-spsm-2.0.0-SNAPSHOT.jar;"%REPO%"\jgrapht-0.6.0.jar;"%REPO%"\s-match-spsm-prolog-2.0.0-SNAPSHOT.jar;"%REPO%"\s-match-nlp-2.0.0-SNAPSHOT.jar;"%REPO%"\s-match-nlp-opennlp-2.0.0-SNAPSHOT.jar;"%REPO%"\opennlp-tools-1.5.3.jar;"%REPO%"\jwnl-1.3.3.jar;"%REPO%"\opennlp-maxent-3.0.3.jar;"%REPO%"\s-match-nlp-annotation-2.0.0-SNAPSHOT.jar;"%REPO%"\slf4j-api-1.7.21.jar;"%REPO%"\slf4j-log4j12-1.7.21.jar;"%REPO%"\log4j-1.2.17.jar;"%REPO%"\s-match-utils-2.0.0-SNAPSHOT.jar

set ENDORSED_DIR=
if NOT "%ENDORSED_DIR%" == "" set CLASSPATH="%BASEDIR%"\%ENDORSED_DIR%\*;%CLASSPATH%

if NOT "%CLASSPATH_PREFIX%" == "" set CLASSPATH=%CLASSPATH_PREFIX%;%CLASSPATH%

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS% -Xmx512M -classpath %CLASSPATH% -Dapp.name="match-manager" -Dapp.repo="%REPO%" -Dapp.home="%BASEDIR%" -Dbasedir="%BASEDIR%" it.unitn.disi.smatch.CLI %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
