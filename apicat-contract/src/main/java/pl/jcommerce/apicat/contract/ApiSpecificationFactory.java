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
            if (typeMap == null)
                typeMap = initTypeMap();
            if (typeMap.get(type) == null)
                throw new RuntimeException("Unable to find ApiSpecification implementation for type: " + type);
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
        if (typeMap == null)
            typeMap = initTypeMap();

        return typeMap.keySet();
    }

    /**
     * Prepare type -> ApiSpecification implementation mapping
     *
     * @return type -> ApiSpecification implementation map
     */
    private static Map<String, Class<? extends ApiSpecification>> initTypeMap() {
        Map<String, Class<? extends ApiSpecification>> specTypeMap = new HashMap<>();
        ServiceLoader.load(ApiSpecification.class).forEach(apiSpecification -> {
            if (specTypeMap.containsKey(apiSpecification.getType()))
                throw new RuntimeException("Duplicated ApiSpecification implementation with type: " + apiSpecification.getType());
            specTypeMap.put(apiSpecification.getType(), apiSpecification.getClass());
        });

        return specTypeMap;
    }


}

