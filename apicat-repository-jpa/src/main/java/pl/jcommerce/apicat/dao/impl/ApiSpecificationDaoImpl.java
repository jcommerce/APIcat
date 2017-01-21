package pl.jcommerce.apicat.dao.impl;

import org.springframework.stereotype.Repository;
import pl.jcommerce.apicat.dao.ApiSpecificationDao;
import pl.jcommerce.apicat.model.ApiSpecificationModel;

@Repository("apiSpecificationDao")
public class ApiSpecificationDaoImpl extends GenericDaoImpl<ApiSpecificationModel> implements ApiSpecificationDao {
}
