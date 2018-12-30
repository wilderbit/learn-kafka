Basic Producer configuration

Cluster setup
- Three Partitions
- Three brokers
- Replication factor: 3


Start Zookeeper
---------------
bin/zookeeper-server-start.sh config/zookeeper.properties

Start Broker Server
-------------------
bin/kafka-server-start.sh config/server_1.properties

Create Topic
------------
bin/kafka-topics.sh --create --topic my-topic --zookeeper localhost:2181 --replication-factor 3 --partitions 3

Describe Topic
--------------
bin/kafka-topics.sh --describe --topic my-topic --zookeeper localhost:2181

Topic list
-----------
bin/kafka-topics.sh --list --zookeeper localhost:2181

To start Producer
-----------------
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic my-topic

Consumer Console
————————--------
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic my-topic