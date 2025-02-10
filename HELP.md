# Getting Started

## Containerization

### Docker for Desktop on Macs

Remember that _Docker for Desktop_ on a Mac runs inside a virtual machine.

To be able to access the guest machine where docker is running and is holding your
containers' volumes, run the following

```shell
docker run -it --privileged --pid=host debian nsenter -t 1 -m -u -n -i sh
```

### Image building (for docsapp-fe integration)

```shell
docker build -t nekosoft/chr-backend:v1 .
```

## Instrumentation

### Configuring Prometheus

You must have Docker installed and running.

Create a Prometheus configuration file that connects to the Spring Boot application

```yaml
scrape_configs:
  - job_name: 'companieshouse'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s # This can be adjusted based on our needs
    static_configs:
      - targets: ['host.docker.internal:8080']
```

Run Prometheus with Docker pointing to the config file you just created in a Docker volume.

```shell
sudo docker run --name prometheus -d -p 9090:9090 -v ./prometheus-springboot3.yaml:/etc/prometheus/prometheus.yml --add-host host.docker.internal=host-gateway prom/prometheus
```

Check the configuration was correctly loaded with

```shell
curl -X GET http://localhost:9090/api/v1/status/config
```

Stop the Prometheus container, remove it and then start it anew

```shell
# give it more time to shut down properly if you plan to restart it
sudo docker stop prometheus --time 30
sudo docker rm prometheus
sudo docker run --name prometheus -d -p 9090:9090 -v ./prometheus-springboot3.yaml:/etc/prometheus/prometheus.yml --add-host host.docker.internal=host-gateway prom/prometheus
```

You can get access to a terminal in the Docker container with

```shell
sudo docker exec -ti prometheus sh
```

### Configuring the Spring Boot application

Add the dependencies on Actuator, Micrometer and Prometheus registry to your POM file.

```xml
		<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

		<dependency>
			<groupId>io.micrometer</groupId>
			<artifactId>micrometer-registry-prometheus</artifactId>
		</dependency>
		<dependency>
			<groupId>io.micrometer</groupId>
			<artifactId>micrometer-tracing-bridge-otel</artifactId>
		</dependency>
		<dependency>
			<groupId>io.opentelemetry</groupId>
			<artifactId>opentelemetry-exporter-otlp</artifactId>
		</dependency>
```

Then configure your application.yaml file to export metrics to Prometheus.

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: "always"
```

This endpoint will provide the details of the metrics available to Prometheus.

```shell
curl -X GET http://localhost:8080/actuator/prometheus
```

This is also the URL that will be scraped by Prometheus at regular intervals looking for metrics.
