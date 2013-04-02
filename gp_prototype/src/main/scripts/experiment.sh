#!/bin/bash

java -jar lib/gp_prototype-1.0-SNAPSHOT.jar config/remote_monitoring_data.properties >& full_data.log &
java -jar lib/gp_prototype-1.0-SNAPSHOT.jar config/remote_monitoring_data_moving_window.properties >& moving_window.log &
