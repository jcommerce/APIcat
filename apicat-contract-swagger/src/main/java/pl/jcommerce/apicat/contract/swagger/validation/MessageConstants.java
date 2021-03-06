package pl.jcommerce.apicat.contract.swagger.validation;

/**
 * Created by krka on 14.10.2016.
 */
public class MessageConstants {

    public final static String INCONSISTENT_CONSUMER_CONTRACT = "Consumer contract is inconsistent with Swagger Specification";

    public final static String INCONSISTENT_PROVIDER_CONTRACT = "Provider contract is inconsistent with Swagger Specification";

    public final static String WRONG_HOST_ADDRESS = "Wrong host address {0} instead of {1}";

    public final static String WRONG_HOST_PATH = "Wrong host path {0} instead of {1}";

    public final static String WRONG_HOST_SCHEMES = "Wrong host schemes {0} instead of {1}";

    public final static String ENDPOINT_NOT_USED = "Endpoint {0} is not used";

    public final static String OPERATION_NOT_USED = "Method {0} with the operation ID = {1} from {2} endpoint is not used";

    public final static String OPERATION_NOT_EXISTS = "Operation with ID = {0} not exists";

    public final static String PARAMETER_NOT_USED = "Parameter {0} from the method {1} with the operation ID = {2} from {3} endpoint is not used";

    public final static String RESPONSE_NOT_USED = "Response with the code {0} and the message {1} from the method {2} with the operation ID = {3} from {4} endpoint is not used";

    public final static String DEFINITION_NOT_USED = "Definition {0} is not used";

    public final static String PROPERTY_NOT_USED = "Property {0} in the definition {1} is not used";

    public final static String PROPERTY_NOT_EXISTS = "Property {0} in the definition {1} not exists";

    public final static String PROPERTY_WRONG_TYPE = "Property {0} has wrong type";
}
