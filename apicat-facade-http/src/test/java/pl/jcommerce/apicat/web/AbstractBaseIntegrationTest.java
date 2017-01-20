package pl.jcommerce.apicat.web;


import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class AbstractBaseIntegrationTest {

    @LocalServerPort
    private Integer serverPort;

    @Before
    public void setup(){
        RestAssured.port = serverPort;
    }

    @Autowired
    protected WebApplicationContext context;

    protected String getSwaggerDefinitionFromFile(String filename) {
        String content;

        try {
            File contractsFile = new File(ClassLoader.getSystemResource("contracts").toURI());
            String filePath = contractsFile.getPath() + File.separator + filename;
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return content;
    }
}
