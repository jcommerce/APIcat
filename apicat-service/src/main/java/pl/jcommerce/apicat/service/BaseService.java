package pl.jcommerce.apicat.service;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

public class BaseService {

    protected Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
}
