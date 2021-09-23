cd caychen-boot-parent
mvn clean install -Dmaven.test.skip=true

cd ../caychen-boot-common
mvn clean install -Dmaven.test.skip=true

cd ../caychen-boot-web
mvn clean install -Dmaven.test.skip=true

cd ../caychen-boot-file
mvn clean install -Dmaven.test.skip=true

cd ../caychen-boot-redis
mvn clean install -Dmaven.test.skip=true