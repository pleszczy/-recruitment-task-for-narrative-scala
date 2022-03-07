# Overview
This is a solution to a recruitment task for narrative

## TODO
- setup querying druid
- setup swagger
- add unit/integration tests
- containerize the project (docker)

## How to run
- start docker and kafka `docker-compose -f "./local-environment/docker-compose.yml" up`
- start the application `sbt run`
- upload the druid ingestion spec `curl -X POST -H 'Content-Type: application/json' -d @analytics-v1.json http://localhost:8081/druid/indexer/v1/supervisor`

## API
TODO: Move to swagger
### Rest
```shell
http get "http://localhost:8080/analytics?timestamp=$(date +%s)"
http post "http://localhost:8080/analytics?timestamp=$(date +%s)&user=piotr&event=click"
````
![image](https://user-images.githubusercontent.com/476428/157098990-8a4a0684-e602-491b-b4e6-06813d5998f5.png)
### Druid
[Druid console](http://localhost:8888/unified-console.html#query)

![image](https://user-images.githubusercontent.com/476428/157099307-08304548-f1d6-4b54-9c00-1c23f3e47737.png)

### The task
As a part of integrating with our partners, Narrative supports collecting data on website visitors and returning some basic analytics on those visitors. The goal of this task is to implement a basic endpoint for this use case. It should accept the following over HTTP:

```
POST /analytics?timestamp={millis_since_epoch}&user={user_id}&event={click|impression}
GET /analytics?timestamp={millis_since_epoch}
```

When the POST request is made, a 204 is returned to the client with an empty response. We simply side-effect and track the event in our data store.

When the GET request is made, we return information in the following format to the client, for the hour (assuming GMT time zone) of the requested timestamp:

```
unique_users,{number_of_unique_usernames}
clicks,{number_of_clicks}
impressions,{number_of_impressions}
```

It is worth noting that the traffic pattern is typical of time series data. The service will receive many more GET requests (~95%) for the current hour than for past hours (~5%). The same applies for POST requests.

Please ensure that the code in the submission is fully functional on a local machine, and include instructions for building and running it. Although it should still pass muster in code review, it is fine for the code to not be completely production ready in the submission. For example, using local storage like in-memory H2 instead of dedicated MySQL is OK. As a guide for design decisions, treat this exercise as the initial prototype of an MVP that will need to be productionalized and scaled out in the future, and be prepared for follow-up discussion on how that would look.