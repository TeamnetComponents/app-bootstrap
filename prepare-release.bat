cd ../bootstrap-dependencies
call mvn release:prepare -DskipTests=true
call mvn release:perform -DskipTests=true
call mvn clean install -DskipTests=true
cd ../app-bootstrap-commons
call mvn release:prepare -DskipTests=true
call mvn release:perform -DskipTests=true
cd ../app-bootstrap-dictionary
call mvn release:prepare -DskipTests=true
call mvn release:perform -DskipTests=true
cd ../app-bootstrap-security
call mvn release:prepare -DskipTests=true
call mvn release:perform -DskipTests=true
cd ../app-bootstrap-web
call mvn release:prepare -DskipTests=true
call mvn release:perform -DskipTests=true
cd ../app-bootstrap
call mvn release:prepare -DskipTests=true
call mvn release:perform -DskipTests=true
