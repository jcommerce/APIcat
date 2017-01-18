package pl.jcommerce.apicat.dao.impl;

import org.springframework.stereotype.Repository;
import pl.jcommerce.apicat.dao.ApiDefinitionDao;
import pl.jcommerce.apicat.model.ApiDefinitionModel;

/**
 * Created by prho on 17.01.17.
 */

@Repository("apiDefinitionDao")
public class ApiDefinitionDaoImpl extends GenericDaoImpl<ApiDefinitionModel> implements ApiDefinitionDao {
}
