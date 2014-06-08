#!/bin/bash

SLEEPTIME=1890

if [ -z $1 ]; then
        echo "Please provide number of repetitions as first argument"
        exit
fi

for I in `seq $1`; do
        echo "Attempt: $I";
        java -jar lib/hubert-2.0-SNAPSHOT.jar config/remote_monitoring_data.properties >& full_data.log.$I &
        java -jar lib/hubert-2.0-SNAPSHOT.jar config/remote_monitoring_data_moving_window.properties >& moving_window.log.$I &
        echo "Sleeping: $SLEEPTIME"
        sleep $SLEEPTIME
done;
