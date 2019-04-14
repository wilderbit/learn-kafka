# Basic Producer configuration


Cluster setup
-------------
- Three Partitions
- Three brokers
- Replication factor: 3


Start Zookeeper
---------------
- `bin/zookeeper-server-start.sh config/zookeeper.properties`

Start Broker Server
-------------------
- `bin/kafka-server-start.sh config/server_1.properties`

Create Topic
------------
- `bin/kafka-topics.sh --create --topic my-topic --zookeeper localhost:2181 --replication-factor 3 --partitions 3`

Describe Topic
--------------
- `bin/kafka-topics.sh --describe --topic my-topic --zookeeper localhost:2181`

Topic list
-----------
- `bin/kafka-topics.sh --list --zookeeper localhost:2181`

To start Producer
-----------------
- `bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-topic`

Consumer Console
----------------
- `bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic my-topic`


# Basic consumer setup

Cluster setup
-------------
- Single broker
- Two topics
- Three Partitions per topic
- Single replication factor

Look for: 
--------
- kafka-producer-perf-test.sh
- subscribe() and assign()
- Add new Partitions 
- Compare consumer output

Topic creation
---------------
- `bin/kafka-topics.sh --create --topic my-topic --zookeeper localhost:2181 --replication-factor 1 --partitions 3`
- `bin/kafka-topics.sh --create --topic my-other-topic --zookeeper localhost:2181 --replication-factor 1 --partitions 3`

Describe topics
--------------
- `bin/kafka-topics.sh --describe --zookeeper localhost:2181`

Perf-test-tool
--------------

- `bin/kafka-producer-perf-test.sh --topic my-other-topic --num-records 50 --record-size 1 --throughput 10 --producer-props bootstrap.servers=localhost:9093 key.serializer=org.apache.kafka.common.serialization.StringSerializer value.serializer=org.apache.kafka.common.serialization.StringSerializer`
- `bin/kafka-producer-perf-test.sh --topic my-topic --num-records 50 --record-size 1 --throughput 10 --producer-props bootstrap.servers=localhost:9093 key.serializer=org.apache.kafka.common.serialization.StringSerializer value.serializer=org.apache.kafka.common.serialization.StringSerializer`


Alter kafka topic partitions
----------------------------
- `bin/kafka-topics.sh --zookeeper localhost:2181 --topic my-topic --alter --partitions 4`


# Demo for Consumer Group

Setup
------
- Three consumers with same group id
- consumes a single topic with three partitions

Look for ->
-----------
-  Shared topic consumption
- Adding an additional consumer
- Adding an additional partition 
  
  
Workflow
--------
- start zookeeper
- start two broker server with port 9093 and 9094
- create topic with 3 partition
- Create 4 Consumer App with same group.id
- Create one producer
- Start 3 java app consumers
- start single producer 
- All three consumers will consume each partitions
- Now start 4th consumer and run again producer so one of the consumer will sit idle
- Alter partition 3 to 4
- Run again producer and see the output of all 4 consumers
- Now kill one of the consumer so the scenario is 4 partition and 3 consumers
- Run again producer and see the output of all 3 running consumers
  
