package pl.jcommerce.apicat.web;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class AbstractBaseIntegrationTest {

    @LocalServerPort
    private Integer serverPort;

    @Before
    public void setup() {
        RestAssured.port = serverPort;
    }

    protected String getFilepath(String filename) {
        ClassLoader classLoader = AbstractBaseIntegrationTest.class.getClassLoader();
        return classLoader.getResource("contracts/" + filename).getFile();
    }
}
