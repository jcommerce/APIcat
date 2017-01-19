package pl.jcommerce.apicat.service.apicontract;

import pl.jcommerce.apicat.service.apicontract.dto.ApiContractCreateDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractDto;
import pl.jcommerce.apicat.service.apicontract.dto.ApiContractUpdateDto;

/**
 * Created by luwa on 18.01.17.
 */
public interface ApiContractService {

    Long createContract(ApiContractCreateDto apiContractDto);

    ApiContractDto getContract(Long id);

    void updateContract(Long id, ApiContractUpdateDto apiContractDto);

    void deleteContract(Long id);
}
