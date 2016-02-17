# flexget-series-ui

### Continuous Integration Server
http://recknerd.com/jenkins [![Build Status](https://recknerd.com/jenkins/job/flexget-series-ui+ci/badge/icon?style=plastic)](https://recknerd.com/jenkins/job/flexget-series-ui+ci)

![flexget-series-ui Welcome](https://github.com/jreckner/flexget-series-ui/raw/master/docs/FlexGetFullScreen.png)

### FlexGet Config - Current Limitations
At this point in time, this application only supports the following layout including required key names

```yml
templates: <-- required
  global:
    <anything>
  tv: <-- required
    thetvdb_lookup: true <-- optional
    verify_ssl_certificates: true <-- optional
    quality: "720p" <-- optional
    series: <-- required
      hdGroup: <-- hdGroup or stdGroup required
        - Title 1
        - Title 2: <-- settings optional (exact and quality currently supported)
          exact: true
      stdGroup: <-- hdGroup or stdGroup required
        - Title 3
          quality: "sdtv+"
    settings: <-- optional
      <anything>
    exists_series: <-- optional
        <anything>
    set:
      <anything>
    transmission: <-- optional
      <anything>
    clean_transmission: <-- optional
      <anything>
    email: <-- optional
      <anything>
tasks:
  <anything>
email: <-- optional
  <anything>
```
### Install
  * Requires Java8 (Ubuntu I recommend [webupd8team described here](http://www.webupd8.org/2012/01/install-oracle-java-jdk-7-in-ubuntu-via.html) simply replace java7 with java8)
  * Get the latest version from the [recknerd.com Jenkins build machine](https://www.recknerd.com/jenkins/job/flexget-series-ui+ci/lastSuccessfulBuild/) or directly as described below:
    * wget https://www.recknerd.com/jenkins/job/flexget-series-ui+ci/lastSuccessfulBuild/artifact/build/libs/flexget-series-ui-<version>-SNAPSHOT.jar
  * Place the jar in the same directory as your flexget config file (config.yml by default)
    * You can override this location and filename using a custom application.yml
    * In the same directory as the jar file create a `application.yml`
      * `echo "flexget.config: /home/user/.flexget/custom.yml" > application.yml`
  * Run `java -jar flexget-series-ui-<version>-SNAPSHOT.jar &`
  * Browse to `http://localhost:1981`
