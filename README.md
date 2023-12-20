# simple-pub-subscribe

- run kafka `docker-compose up`
- consume:
```shell
docker run --rm -it \
  --network=host \
  confluentinc/cp-kafka:6.2.0 \
  kafka-console-consumer \
    --bootstrap-server localhost:9092 \
    --topic USER_EVALUATION_SUBMITTED \
    --from-beginning \
    --group your-consumer-group-id
```
- GET messages
```shell
curl -H "Content-Type: application/json" 'http://localhost:8080/employees'
```
- POST a message
```shell
curl -X POST -H "Content-Type: application/json" 'http://localhost:8080/employees' -d '{"compensation": 10}'
```
