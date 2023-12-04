# Contube

ConTube is a scalable data connector framework that facilitates efficient data transfer between diverse systems.

## Motivation

The challenge of efficiently moving data from one system to another has long been a significant issue in data
engineering. ConTube seeks to address this by offering a generic data connector framework capable of handling data from
diverse system.

## How it Works

ConTube provides a universal set of data connection protocols. These protocols define how to extract data from a system,
handle data formatting, and write data into other systems. This framework enables us to implement Runtimes for Pulsar
Connectors and Kafka Connectors, thereby leveraging the rich data connection ecosystem of Pulsar and Kafka. This
facilitates data migration capabilities between a variety of different systems.

ConTube operates through two main components: `Con` and `Tube`. A `Con` serves as a connection for connecting and moving
data between tubes. A `Tube` is a data processing unit that can be connected to other data systems and other tubes to
form a data processing pipeline. A `Tube` can be either a `Source` or a `Sink`. A `Source` tube reads data from an
upstream system and provides it for further processing. A `Sink` tube receives data from other tubes and writes it to a
downstream system.

This repository defines the core interfaces for `ConTube`. Here's a high-level overview of how these interfaces work
together:

1. `Con Interface`: The Con interface serves as a connection for connecting and exchanging data between tubes. It
   provides the send method for dispatching records to other tubes and the register method for processing incoming
   records.
2. `Source Tube Interface`: The Source interface is implemented by a source tube, which reads records from an upstream
   system and provides them for further processing.
3. `Sink Tube Interface`: The Sink interface is implemented by a sink tube, which receives records from other tubes and
   writes them to a downstream system.
4. `TubeRecord Interface`: The TubeRecord interface encapsulates the data transferred between tubes in the system. It
   provides methods to access the value of the record, its schema data, its index, and to handle its commit state.
5. `Context Interface`: The Context interface provides an interaction medium for a tube with its runtime environment. It
   allows the tube to access its name and control its execution state through the getName, stop, and fail methods.

## Features

- **Universal Data Format**: ConTube offers a universal data format capable of representing data from virtually any
  system. This allows us to implement Runtimes for Pulsar Connectors and Kafka Connectors, thereby accessing the rich
  data connection ecosystem of Pulsar and Kafka. This feature is still under development.
- **Data Sourcing and Sinking**: ConTube provides a set of interfaces for data sourcing and sinking. **This allows us to
  implement Runtimes for Pulsar Connectors and Kafka Connectors**, thereby accessing the rich data connection ecosystem
  of Pulsar and Kafka. Support for other MQ ecosystems is on the way.
- **Lightweight Runtime**: In contrast to the Kafka and Pulsar Connect Frameworks, Contube allows for running multiple
  tubes within a single JVM process or a single Docker container, eliminating the need for running separate MQ clusters.
- **Expanded Data Transfer Options**: The current implementation of `Con` is MemoryCon, implying that data is
  transferred in memory. We aim to add more `Con` implementations, such as GrpcCon, which transfers data using gRPC, and
  MQCon, which transfers data using various message queue protocols.
- **Data Consistency Guarantee**: A work in progress feature, we aim to introduce a data consistency guarantee mechanism
  to ensure that data is transferred between systems in an `exactly once`, `at least once`, or `at most once` manner.
- ... and more.

The repository [contube-thirdparty](https://github.com/RobertIndie/contube-thirdparty) provides serveral implementations
of `Tube`. These include tubes for executing Kafka Connectors and Pulsar IO Connectors, as well as tubes for interacting
with the Pulsar system. We are working on additional implementations.

## Example

TL;DR. For a basic overview, please refer to this example.

This example will demonstrate how to:

- Capture data changes from MongoDB using the Kafka Debezium MongoDB connector
- Transfer the data using MemoryCon (The default implementation of Con interface)
- Sink the data into Elastic-Search using the Pulsar Elastic-Search connector

### Download the Example

To begin, download this example
from [here](https://github.com/RobertIndie/contube-thirdparty/tree/main/examples/mongodb-elasticsearch). You can clone
the [contube-thirdparty repo](https://github.com/RobertIndie/contube-thirdparty) using git:

```sh
git clone git@github.com:RobertIndie/contube-thirdparty.git
cd examples/mongodb-elasticsearch
```

This example provides everything needed for setup. If you only want to run and validate this example, you can skip ahead
to the [`Run and Validate the Example`](#run-and-validate-the-example) section.

Before running the example, let's examine the example files.

In the [tubes](https://github.com/RobertIndie/contube-thirdparty/tree/main/examples/mongodb-elasticsearch/tubes)
directory, we have defined two tubes that function as data connectors.

The MongoDB source tube uses the Kafka Debezium MongoDB connector to capture data changes from MongoDB. The tube
configuration is located in the `mongodb-source.yaml` file:

```yaml
name: mongo-source
type: source
class: com.zikeyang.contube.kafka.connect.source.KafkaConnectSourceTube
sinkTubeName: es-sink
config:
  mongodb.hosts: "rs0/mongodb:27017"
  mongodb.connection.string: "mongodb://mongodb:27017/?replicaSet=rs0"
  mongodb.user: "debezium"
  mongodb.password: "dbz"
  mongodb.authsource: "admin"
  mongodb.name: "debezium"
  mongodb.task.id: "1"
  task.class: "io.debezium.connector.mongodb.MongoDbConnectorTask"
```

This tube uses the `KafkaConnectSourceTube` class, an implementation for Kafka Connect. The `sinkTubeName` is the name
of the sink tube where this source tube will transfer the data. The `config` section provides the configuration for the
Kafka Debezium MongoDB connector. More details about the configuration can be found in
the [Kafka Debezium MongoDB](https://debezium.io/documentation/reference/1.9/connectors/mongodb.html) connector
documentation.

The Elastic Search sink tube uses the Pulsar Elastic Search connector to sink the data into Elastic Search. The tube
configuration is located in the `es-sink.yaml` file:

```yaml
name: es-sink
type: sink
class: com.zikeyang.contube.pulsar.connect.sink.PulsarConnectSinkTube
config:
  archive: "./tubes/pulsar-io-elastic-search-3.1.1.nar"
  connectorConfig:
    elasticSearchUrl: "http://elastic-search:9200"
    indexName: "my_index"
    username: "elastic"
    password: "ElasticPasseword"
```

This tube uses the `PulsarConnectSinkTube` class, an implementation for Pulsar IO. The `archive` is the path of the
Pulsar Elastic Search connector nar file. The `connectorConfig` section provides the configuration for the Pulsar
Elastic Search connector. More details about the configuration can be found in
the [Pulsar Elastic Search](https://pulsar.apache.org/docs/en/io-elastic-search-sink/) connector documentation.

In the `docker-compose.yaml` file, we illustrate how to use Contube for this example:

```yaml
    contube-test:
      image: contube/contube-all
      volumes:
        - ./tubes:/contube/tubes
      depends_on:
        - mongodb
        - elastic-search
```

We've mounted the `tubes` directory to the `/contube/tubes` directory in the container. Therefore, Contube will load the
tube configurations from the `tubes` directory and start all tubes therein.

To run this example, first, download the Pulsar Elastic Search connector nar file and place it into the Contube
container. For the Kafka Debezium connector jar file, we've already included it in the Contube container. However, if
you wish to use other Kafka or Pulsar connectors, simply place the connector jar or nar file into the `/contube/libs`
path of the Contube container.

### Run and Validate the Example

A script has been prepared for your convenience. You can run `./run.sh` to execute all necessary tasks.

```shell
./run.sh
```

Finally, let's validate this example.

Insert a record into MongoDB:

```shell
# Please ensure you are running this command in the `examples/mongodb-elasticsearch` directory.
docker-compose exec mongodb bash -c 'mongo -u debezium -p dbz --authenticationDatabase admin inventory'

db.customers.insert([
    { _id : NumberLong("1005"), first_name : 'Bob', last_name : 'Hopper', email : 'thebob@example.com', unique_id : UUID() }
]);
```

Check the data in Elastic Search:

```shell
# Refresh the index:
curl -s http://localhost:9200/my_index/_refresh

# Search documents:
curl -s http://localhost:9200/my_index/_search
```

The search should return data containing the following document:

```json
{
  "_index": "my_index",
  "_type": "_doc",
  "_id": "xnhDMIwBlImub__wU9mI",
  "_score": 1.0,
  "_source": {
    "after": "{\"_id\": {\"$numberLong\": \"1005\"},\"first_name\": \"Bob\",\"last_name\": \"Hopper\",\"email\": \"thebob@example.com\",\"unique_id\": {\"$binary\": \"ZtUqTQYzSCq5xfpaSlM/3w==\",\"$type\": \"04\"}}",
    "source": {
      "version": "1.9.7.Final",
      "connector": "mongodb",
      "name": "debezium",
      "ts_ms": 1701616760000,
      "snapshot": "true",
      "db": "inventory",
      "rs": "rs0",
      "collection": "customers",
      "ord": 1
    },
    "op": "r",
    "ts_ms": 1701616764265
  }
}
```

You can also experiment with running other Kafka/Pulsar connectors on Contube. Simply place the connector jar/nar files
into the `/contube/libs` path of the Contube container, add the tube configuration into the `tubes` directory, and then
run the Contube container.

Note: If you encounter connection issues with the Contube container, try restarting the container by
running `docker-compose restart contube`. If you encounter other issues, please feel free to open an issue.

## How to Build

To compile Contube, execute the following command:

```shell
./gradlew jar
```

Next, place your tube implementation jar file into the `libs` directory and add your tubes configuration files. Start
the runtime by executing the following command:

```shell
bin/runtime.sh conf/runtime.yaml path/to/your-tube-config.yaml
```

We have prepared a straightforward file source and sink example in the `examples` directory. You can initiate the
example by running the following command:

```shell
bin/runtime.sh conf/runtime.yaml examples/file-source.yaml examples/file-sink.yaml
```

This example will duplicate the content from `examples/source.txt` to `examples/test-result-sink.txt`.

## Future Work

This project is currently in the Proof of Concept (POC) stage. We warmly welcome all contributors who are interested in
this project.

Here are some of the features we plan to introduce in the future:

- [ ] Universal Data Format
- [ ] Pluggable Data Schema
- [ ] Additional Con implementations: GrpcCon, MQCon, etc.
- [ ] Distributed offset store implementation
- [ ] Data consistency guarantee mechanism
- [ ] Awaiting more ideas...

## Contributing

We enthusiastically welcome contributions from the community. If you discover a bug or have a feature request, please
open an issue or submit a pull request.

## License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
