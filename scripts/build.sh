#!/bin/bash
CURRENT_DIR=$PWD
# Go to src
cd ../application

echo -e "-------------Mvn install-------------"
mvn clean install
echo -e "-------------Copy to application-------------"
cp target/hf-mvp-0.0.1-SNAPSHOT-jar-with-dependencies.jar blockchain-client.jar

# Restore path
cd "$CURRENT_DIR"