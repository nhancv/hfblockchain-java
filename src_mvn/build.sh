#!/bin/bash
CURRENT_DIR=$PWD
# Go to src
cd ../

echo -e "-------------Mvn install-------------"
mvn clean install
echo -e "-------------Copy to root path-------------"
cp target/hf-mvp-0.0.1-SNAPSHOT-jar-with-dependencies.jar blockchain-client.jar

# Restore path
cd "$CURRENT_DIR"