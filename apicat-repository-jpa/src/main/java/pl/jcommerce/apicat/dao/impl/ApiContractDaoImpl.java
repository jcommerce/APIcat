package pl.jcommerce.apicat.dao.impl;

import org.springframework.stereotype.Repository;
import pl.jcommerce.apicat.dao.ApiContractDao;
import pl.jcommerce.apicat.model.ApiContractModel;

@Repository("apiContractDao")
public class ApiContractDaoImpl extends GenericDaoImpl<ApiContractModel> implements ApiContractDao {
}
