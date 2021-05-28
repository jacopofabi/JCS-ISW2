PATH_JACOCO_CLI_JAR="C:/GithubRepo/JCS-ISW2/src/test/lib"
PATH_JCS_SRC="C:/GithubRepo/JCS-ISW2/src/test"
PATH_JCS_JAR="C:/GithubRepo/JCS-ISW2/src/test/lib"
#PATH_JCS_FAT_JAR="ToBeDefined"

## CREAZIONE CON JACOCO DA CLI DEL FAT-JAR INSTUMENTATO:
## (https://www.jacoco.org/jacoco/trunk/doc/cli.html)

#java -jar ${PATH_JACOCO_CLI_JAR}/jacococli.jar instrument ${PATH_JCS_JAR}/jcs-1.3.jar --dest ${PATH_JCS_FAT_JAR}

## *****************************************************************
## *****************************************************************

## ESTRAZIONE CON JACOCO DA CLI DEL REPORT:
## (https://www.jacoco.org/jacoco/trunk/doc/cli.html)

#mkdir -p target/jacoco-gen/jcs-coverage/

java -jar ${PATH_JACOCO_CLI_JAR}/jacococli.jar report target/jacoco.exec --classfiles ${PATH_JCS_JAR}/jcs-1.3.jar --sourcefiles ${PATH_JCS_SRC} --html target/jacoco-gen/jcs-coverage/ --xml target/jacoco-gen/jcs-coverage/file.xml --csv target/jacoco-gen/jcs-coverage/file.csv