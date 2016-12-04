package pl.jcommerce.apicat.contract;

/**
 * Defines ApiSpecification stages
 *
 * @author Daniel Charczyński
 */
public enum ApiSpecificationStage {
    /**
     * ApiSpecification definition must be valid
     */
    DRAFT,
    /**
     * Must have ApiContract.
     * Connected ApiDefinition must be RELEASED
     * ApiSpecification, its contract and ApiDefinition must be valid.
     */
    RELEASED
}
