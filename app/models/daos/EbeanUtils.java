package models.daos;

import com.avaje.ebean.Ebean;

import exceptions.DAOException;

public class EbeanUtils {
    
    public static <T> T findOrException(Class<T> beanType, Object id){
        T found = Ebean.find(beanType, id);
        if (found != null)
            return found;
        else
            throw new DAOException.NoEncontradoException(beanType);
    }

}
