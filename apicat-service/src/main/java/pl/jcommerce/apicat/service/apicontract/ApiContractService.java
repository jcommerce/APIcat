package pl.jcommerce.apicat.service.apicontract;

import pl.jcommerce.apicat.contract.ApiContract;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractCreateDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractUpdateDto;

/**
 * Created by luwa on 18.01.17.
 */
public interface ApiContractService {

    Long createContract(ApiContractCreateDto data);

    ApiContract getContract(Long id);

    Long updateContract(ApiContractUpdateDto data);

    void deleteContract(Long id);
}
