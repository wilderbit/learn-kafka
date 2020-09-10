# Running Kafka cluster


### Prerequisite
- Install Java
- Install maven(optional)
- Download kafka binaries from and unzip
  `https://www.apache.org/dyn/closer.cgi?path=/kafka/2.6.0/kafka_2.13-2.6.0.tgz`

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
  
## Producer Config: Required properties
- bootstrap.servers: list of servers
- key.serializer
- value.serializer
- Refer https://kafka.apache.org/documentation/#producerconfigs





## Running Java class directly from command line
- mvn clean install -DskipTests
- mvn exec:java -Dexec.mainClass=KafkaProducerApp -Dexec.args="foo bar"
- mvn test -Dtest=com.mycompany.AppTest#testMethod
- java -cp target/com.vogella.build.maven.java-1.0-SNAPSHOT.jar com.vogella.build.maven.java.App


## Steps to Run cluster and config

Running Cluster
- Zookeeper
- Three brokers
- Three replication factor
- Three partition
- Producer(Console and Java Application)
- Consumer and Consumer Group(Console and Java Application)

Run Zookeeper
--

Zookeeper Config

```properties
dataDir=/tmp/zookeeper
# the port at which the clients will connect
clientPort=2182
# disable the per-ip limit on the number of connections since this is a non-production config
maxClientCnxns=0
# Disable the adminserver by default to avoid port conflicts.
# Set the port to something non-conflicting if choosing to enable this
admin.enableServer=false
# admin.serverPort=8080
```

Run Zookeeper with command

`./bin/zookeeper-server-start.sh config/zookeeper.properties`

Start three brokers
--

Broker Config

`/bin/kafka-server-start.sh ./config/server_0.properties`
```properties
# The id of the broker. This must be set to a unique integer for each broker.
broker.id=10

listeners=PLAINTEXT://127.0.0.1:9092
advertised.listeners=PLAINTEXT://127.0.0.1:9092
num.network.threads=3

# The number of threads that the server uses for processing requests, which may include disk I/O
num.io.threads=8

# The send buffer (SO_SNDBUF) used by the socket server
socket.send.buffer.bytes=102400

# The receive buffer (SO_RCVBUF) used by the socket server
socket.receive.buffer.bytes=102400

# The maximum size of a request that the socket server will accept (protection against OOM)
socket.request.max.bytes=104857600


############################# Log Basics #############################

# A comma separated list of directories under which to store log files
log.dirs=/tmp/kafka-logs_10

# The default number of log partitions per topic. More partitions allow greater
# parallelism for consumption, but this will also result in more files across
# the brokers.
num.partitions=1

# The number of threads per data directory to be used for log recovery at startup and flushing at shutdown.
# This value is recommended to be increased for installations with data dirs located in RAID array.
num.recovery.threads.per.data.dir=1

############################# Internal Topic Settings  #############################
# The replication factor for the group metadata internal topics "__consumer_offsets" and "__transaction_state"
# For anything other than development testing, a value greater than 1 is recommended to ensure availability such as 3.
offsets.topic.replication.factor=3
transaction.state.log.replication.factor=1
transaction.state.log.min.isr=1

log.retention.hours=168

# A size-based retention policy for logs. Segments are pruned from the log unless the remaining
# segments drop below log.retention.bytes. Functions independently of log.retention.hours.
#log.retention.bytes=1073741824

# The maximum size of a log segment file. When this size is reached a new log segment will be created.
log.segment.bytes=1073741824

# The interval at which log segments are checked to see if they can be deleted according
# to the retention policies
log.retention.check.interval.ms=300000

############################# Zookeeper #############################
zookeeper.connect=localhost:2182

# Timeout in ms for connecting to zookeeper
zookeeper.connection.timeout.ms=180000

############################# Group Coordinator Settings #############################
group.initial.rebalance.delay.ms=0
``` 

Create three the config with changing in broker.id and listeners port
- ./config/server_0.properties
- ./config/server_1.properties
- ./config/server_2.properties

Now start all three brokers

```shell script
$ ./bin/kafka-server-start.sh ./config/server_0.properties
$ ./bin/kafka-server-start.sh ./config/server_1.properties
$ ./bin/kafka-server-start.sh ./config/server_2.properties
```

Create Topic
-- 
Create topic `my_topic` with three replication-factor and three replicas

```shell script
$ ./bin/kafka-topics.sh --create --topic my_topic --zookeeper localhost:2182 --replication-factor 3 --partitions 3
``` 

Describe topic with command
```shell script
$ ./bin/kafka-topics.sh --describe --topic my_topic --zookeeper localhost:2182
Topic: my_topic	PartitionCount: 3	ReplicationFactor: 3	Configs:
	Topic: my_topic	Partition: 0	Leader: 11	Replicas: 10,12,11	Isr: 11,10,12
	Topic: my_topic	Partition: 1	Leader: 11	Replicas: 11,10,12	Isr: 11,10,12
	Topic: my_topic	Partition: 2	Leader: 11	Replicas: 12,11,10	Isr: 11,10,12
```

List topic with command
```shell script
$ /bin/kafka-topics.sh --list topic my_topic --zookeeper localhost:2182
```

Delete topic with command(Later you can delete)
```shell script
$ ./bin/kafka-topics.sh --delete --topic my_topic --zookeeper localhost:2182
```

Start console producer/Java Producer
--

```shell script
$ ./bin/kafka-console-producer.sh --bootstrap-server localhost:9092,localhost:9093,localhost:9094 --topic my_topic
```

We can even generate sequence use pipe(|) so feed producer

```shell script
 seq 1 100 | ./bin/kafka-console-producer.sh --bootstrap-server localhost:9093,localhost:9092,localhost:9094 --topic my_topic
```

We can even feed producer from a file, It will take message line by line from file
```shell script
$ cat abc.txt | ./bin/kafka-console-producer.sh --bootstrap-server localhost:9093,localhost:9092,localhost:9094 --topic my_topic
```

Run producer [Java class](./src/main/java/KafkaProducerApp.java)
1. Compile project using maven (`mvn clean install -DskipTests`)
2. Run Producer class(`mvn exec:java -Dexec.mainClass=KafkaProducerApp`)

Start Console Consumer
-- 

```shell script
$ ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092,localhost:9093,localhost:9094 --topic my_topic --from-beginning
```