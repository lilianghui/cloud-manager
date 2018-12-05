spingboot远程调试:
    java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n  -jar gatway-server-1.0-SNAPSHOT.jar
    或 idea new Remote 中的参数 java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000 -jar gatway-server-1.0-SNAPSHOT.jar
 