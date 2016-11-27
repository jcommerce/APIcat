package pl.jcommerce.apicat.contract.swagger.validation;

import java.text.MessageFormat;

/**
 * Created by krka on 23.10.2016.
 */
interface ContractAnalyzer {

    void analyzeContract(Contract contract);

    default void setContractToInvalid(Contract contract) {
        if(contract.isValid())
            contract.setValid(false);
    }

    default void setContractToNotEqual (Contract contract) {
        if(contract.isSwaggerDefinitionsEqual())
            contract.setSwaggerDefinitionsEqual(false);
    }

    default void addContractDifference(Contract contract, String messageConstant, Object... differences) {
        contract.getDiffDetails().addDifference(MessageFormat.format(messageConstant, differences));
    }
}
