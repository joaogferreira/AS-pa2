#!/bin/bash

echo -ne "STOP \n"

echo -ne "Removing topic \n"
./../../kafka_2.13-3.1.0/bin/kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic Sensor > /dev/null 2>&1
sleep 3

echo -ne "Stopping kafka brokers \n"
./../../kafka_2.13-3.1.0/bin/kafka-server-stop.sh > /dev/null 2>&1
sleep 3

