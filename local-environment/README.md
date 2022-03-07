## Druid

### Configuration
Edit `environment` to suite your requirements and hardware capabilities

### Running
- `docker-compose up`

#### Troubleshooting
##### Removing containers
- `docker rm -f $(docker ps -a -q)`
##### Checking whats being send to kafka
- `kcat -b localhost:29092 -t analytics.v1`
##### Deleting kafka ingestion topic
- `kafka-topics --delete --topic analytics-v1 --bootstrap-server localhost:29092`

### Accessing druid console
[Console](http://localhost:8888/unified-console.html)

### Loading data
[![Data loader Kafka](https://user-images.githubusercontent.com/177816/65819337-054eac80-e1d0-11e9-8842-97b92d8c6159.gif)](https://druid.apache.org/docs/latest/ingestion/index.html)

### Querying data
[![Query view combo](https://user-images.githubusercontent.com/177816/65819341-0c75ba80-e1d0-11e9-9730-0f2d084defcc.gif)](https://druid.apache.org/docs/latest/querying/sql.html)

### Druid documentation
- [index](https://druid.apache.org/docs/latest/design/index.html)
- [Docker](https://druid.apache.org/docs/latest/tutorials/docker.html)


### Credits
docker-compose.yaml and environment comes from https://github.com/apache/druid/tree/master/distribution under the Apache License.
