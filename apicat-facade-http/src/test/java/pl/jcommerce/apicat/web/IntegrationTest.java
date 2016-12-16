package pl.jcommerce.apicat.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by jada on 06.12.2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class IntegrationTest {

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    protected String toJson(Object o) {
        String json = "";
        try {
            json = objectMapper.writer().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

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
