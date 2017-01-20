package pl.jcommerce.apicat.mapper;

import org.dozer.CustomConverter;
import pl.jcommerce.apicat.model.ApiContractModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luwa on 20.01.17.
 */
public class ContractIdsConverter implements CustomConverter {

    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Object destination, Object source, Class<?> destinationClass, Class<?> sourceClass) {
        if (source == null) {
            return null;
        }

        List<Long> ids = new ArrayList<>();

        List<ApiContractModel> apiContractModels = (List<ApiContractModel>) source;
        for (ApiContractModel model : apiContractModels) {
            ids.add(model.getId());
        }

        return ids;
    }
}
