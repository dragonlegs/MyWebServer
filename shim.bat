@echo on
rem This is shim.bat
rem Change this to your development directory:
cd C:\Users\Hemanth\IdeaProjects\Distrubuted\MyWebServer\
echo "We are now in a shim called from the Web Browser"
echo Arg one is: %1
rem Change this to point to your Handler directory:
cd C:\Users\Hemanth\IdeaProjects\Distrubuted\MyWebServer\
pause
rem have to set classpath in batch, passing as arg does not work.
rem Change this to point to your own Xstream library files:
set classpath=%classpath%C:\Users\Hemanth\IdeaProjects\Distrubuted\MyWebServer\;C:\Program Files\Java\jdk1.8.0_101\lib\xstream-1.2.1.jar;C:\Program Files\Java\jdk1.8.0_101\lib\xpp3_min-1.1.3.4.O.jar;
rem pass the name of the first argument to java:
java -Dfirstarg=%1 BCHandler
pause
