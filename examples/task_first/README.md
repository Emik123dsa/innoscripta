## The main idea of this solution:

- Use a message brokers:

**Idea:**

The size of file can be not increase some MG, hence,
the process of uploading will not take upon more than couple moments.

In the case, if we want to parse and analyze the file, it can definitely take more time
as it was expected.

As a suggestion, I would like to choose any message brokers and launch it out in other thread:

- [Apache Kafka][apachekafka] - the most preferable for this purpose
- [RabbitMQ][rabbitmq]- AMQP message broker

The only thing that we actually demand to change here is skipping the
next processes after the user was uploading the document.

User will upload one file and immediately receive the notification
that his file was uploading in pending status.

Afterwards, we can put all logic inside of Kafka Consumer.

Meanwhile, the consumer has received the file content OR and will implement further
logic of process, described in the task.

Finally, our steps will be looking like this:

- The user has uploaded the photo and immediately received the notification (The file is still in pending status).
- After Back-End producer will send serialized file (Blob or Base64 or other encoders) to the Apache Kafka Broker.
- Consumer will add new query into Elasticsearch index with pending status, decode dispatched file and send it once more to the other Rest API for analyzing.

For instance, let's create some topics:

(protoc -I=src/main/java/protos/ --java_out=lite:src/main/java/ src/main/java/protos/file.structure.proto)

```sh
# Create topic for file-statuses:
kafka-topics --create --topic data-files-status --partitions 1 --replication-factor 3 --bootstrap-server <kafka_server_host>:<kafka_server_port>

# Create topic for file-structures:
kafka-topics --create --topic data-files-structure --partitions 2 --replication-factor 4 --bootstrap-server <kafka_server_host>:<kafka_server_port>
```

After consumer will be recieved result from the neural network, we have, for instance, 3 options:

1. Timeout
2. File succesfully was parsed
3. Failure

Only in second case state of the Elasticsearch index file will be changed into [SUCCESS],
in other into - FAILURE or different enum.

At the end, we can send an E-mail to inform user about status altering.

Briefly, we can combine them into the next schema:

User's file uploaded -> Dispatch notification to the user in the browser (Http Request has already been finished) -> Meanwhile, serialize or encode PDF file and produce it as a massage to consumer OR store file physically and send only path to the file in system -> Consumer will deserialize OR read file and send it again to the neural API, once he will be recieved new response -> Parse data and save result into other Elasticsearch index of result content and update first index with file status.

| Pros                                                                                         | Cons                                                                   |
| -------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------- |
| The user no longer require to wait in the browser tab, once consumer will digest the message | It still will take some time, if size of the file exceeding than usual |

[//]: # "Message broker references"
[apachekafka]: https://kafka.apache.org/
[rabbitmq]: https://www.rabbitmq.com/
