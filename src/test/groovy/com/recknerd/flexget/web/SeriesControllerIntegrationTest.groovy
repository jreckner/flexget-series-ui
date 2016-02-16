package com.recknerd.flexget.web

import com.jayway.restassured.RestAssured
import com.recknerd.flexget.Application
import groovy.util.logging.Slf4j
import org.apache.http.HttpStatus
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

import static com.jayway.restassured.RestAssured.given
import static org.hamcrest.Matchers.notNullValue

//
// Taken from template found here
// http://www.jayway.com/2014/07/04/integration-testing-a-spring-boot-application/
//

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@ActiveProfiles('test')
@Slf4j
class SeriesControllerIntegrationTest {

    @Value('${local.server.port}')
    int port

    @Autowired
    SeriesController seriesController

    @Before
    public void setUp() {
        RestAssured.port = port
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getAllSeries() {
        given()
            .when()
            .get('/api/series')
            .then()
            .statusCode( HttpStatus.SC_OK )
            .body(notNullValue())
            .body("hdGroup", notNullValue())
            .body("stdGroup", notNullValue())
            .body("available_settings", notNullValue())
    }
}
