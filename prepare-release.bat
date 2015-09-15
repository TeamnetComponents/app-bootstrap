cd ../bootstrap-dependencies
call mvn release:prepare
call mvn release:perform
call mvn clean install
cd ../app-bootstrap-commons
call mvn release:prepare
call mvn release:perform
cd ../app-bootstrap-dictionary
call mvn release:prepare
call mvn release:perform
cd ../app-bootstrap-security
call mvn release:prepare
call mvn release:perform
cd ../app-bootstrap-web
call mvn release:prepare
call mvn release:perform
cd ../app-bootstrap
call mvn release:prepare
call mvn release:perform
