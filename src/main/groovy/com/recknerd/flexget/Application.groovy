package com.recknerd.flexget

import groovy.util.logging.Slf4j
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan
import springfox.documentation.swagger2.annotations.EnableSwagger2

@ComponentScan(basePackages = ['com.recknerd.flexget.*'])
@SpringBootApplication
@EnableSwagger2
@Slf4j
class Application {

    private static ConfigurableApplicationContext ctx

    static void main(String... args) {
        if (args && args[0] == 'stop') {
            System.exit(SpringApplication.exit(ctx))
        } else {
            ctx = SpringApplication.run(Application)
        }
    }
}
