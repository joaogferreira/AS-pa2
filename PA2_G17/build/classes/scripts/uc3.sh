#!/bin/bash
echo -ne "START USE CASE 3 \n"

echo -ne "Starting zookeeper server \n"
./../../kafka_2.13-3.1.0/bin/zookeeper-server-start.sh ./../../kafka_2.13-3.1.0/config/zookeeper.properties > ../../kafka_2.13-3.1.0/logs/zookeeper.log &
sleep 5

echo -ne "Starting kafka server 0 \n"
./../../kafka_2.13-3.1.0/bin/kafka-server-start.sh ./../../kafka_2.13-3.1.0/config/server.properties > ../../kafka_2.13-3.1.0/kafka.log &
sleep 5

echo -ne "Starting kafka server 1 \n"
./../../kafka_2.13-3.1.0/bin/kafka-server-start.sh ./../../kafka_2.13-3.1.0/config/server_1.properties >> ../../kafka_2.13-3.1.0/kafka.log &
sleep 5


echo -ne "Starting kafka server 2 \n"
./../../kafka_2.13-3.1.0/bin/kafka-server-start.sh ./../../kafka_2.13-3.1.0/config/server_2.properties >> ../../kafka_2.13-3.1.0/kafka.log &
sleep 5


echo -ne "Starting kafka server 3 \n"
./../../kafka_2.13-3.1.0/bin/kafka-server-start.sh ./../../kafka_2.13-3.1.0/config/server_3.properties >> ../../kafka_2.13-3.1.0/kafka.log &
sleep 5


echo -ne "Starting kafka server 4 \n"
./../../kafka_2.13-3.1.0/bin/kafka-server-start.sh ./../../kafka_2.13-3.1.0/config/server_4.properties >> ../../kafka_2.13-3.1.0/kafka.log &
sleep 5

echo -ne "Starting kafka server 5 \n"
./../../kafka_2.13-3.1.0/bin/kafka-server-start.sh ./../../kafka_2.13-3.1.0/config/server_5.properties >> ../../kafka_2.13-3.1.0/kafka.log &
sleep 5


echo -ne "Creating topic \n"
./../../kafka_2.13-3.1.0/bin/kafka-topics.sh --create --bootstrap-server localhost:9092  --replication-factor 3 --partitions 3 --topic Sensor --config min.insync.replicas=2


#../../kafka_2.13-3.1.0/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
# lsof -i :8000
#bin/kafka-console-consumer.sh --topic Sensor --bootstrap-server localhost:9092
