package pl.jcommerce.apicat.contract.swagger;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by dach on 2016-11-26.
 */
public class TestUtils {

    public static String getTestConstractsPath() {
        try {
            File contractsFile = new File(ClassLoader.getSystemResource("contracts").toURI());
            return contractsFile.getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
}
