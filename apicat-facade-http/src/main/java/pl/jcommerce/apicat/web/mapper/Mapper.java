package pl.jcommerce.apicat.web.mapper;

import com.google.common.reflect.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;

/**
 * Created by jada on 05.12.2016.
 */

public abstract class Mapper<E, D> {

    @Autowired
    protected ModelMapper modelMapper;

    private Type dtoType = new TypeToken<D>(getClass()){}.getType();
    private Type entityType = new TypeToken<E>(getClass()){}.getType();

    public D getDtoFromEntity(E entity){
        return modelMapper.map(entity, dtoType);
    }

    public E getEntityFromDto(D dto){
        return modelMapper.map(dto, entityType);
    }
}