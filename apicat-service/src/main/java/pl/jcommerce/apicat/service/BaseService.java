package pl.jcommerce.apicat.service;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

/**
 * Created by luwa on 18.01.17.
 */
public class BaseService {

    protected Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
}
