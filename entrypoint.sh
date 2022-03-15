#!/bin/bash

PATH=$PATH:$JAVA_HOME/bin
WATCH_DIR=/usr/local/watcher

local=$(hostname -I)

java -jar WSSinkConnector-0.0.1-jar-with-dependencies.jar $local

while inotifywait -qe modify "$WATCH_DIR"
do
    echo "Directory changed!"
done
