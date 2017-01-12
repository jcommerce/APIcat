package pl.jcommerce.apicat.contract;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Instantiate ApiSpecification implementations only by its type
 * Provides available types
 *
 * @author Daniel Charczyński
 */
public class ApiSpecificationFactory {

    private static Map<String, Class<? extends ApiSpecification>> typeMap = null;

    /**
     * Creates new ApiSpecification instance by {@code type}
     *
     * @param type ApiSpecification implementation type
     * @return ApiSpecification instance
     */
    public static ApiSpecification newInstance(String type) {
        try {
            if (typeMap == null) {
                initTypeMap();
            }
            if (typeMap.get(type) == null) {
                throw new RuntimeException("Unable to find ApiSpecification implementation for type: " + type);
            }
            return typeMap.get(type).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate ApiSpecification", e);
        }
    }

    /**
     * Provides all available ApiSpecification types
     *
     * @return types delivered by implementations
     */
    static Set<String> getTypes() {
        if (typeMap == null) {
            initTypeMap();
        }

        return typeMap.keySet();
    }

    /**
     * Prepare type -> ApiSpecification implementation mapping
     */
    private static void initTypeMap() {
        typeMap = new HashMap<>();
        ServiceLoader.load(ApiSpecification.class).forEach(apiSpecification -> {
            if (typeMap.containsKey(apiSpecification.getType())) {
                throw new RuntimeException("Duplicated ApiSpecification implementation with type: " + apiSpecification.getType());
            }
            typeMap.put(apiSpecification.getType(), apiSpecification.getClass());
        });
    }
}

