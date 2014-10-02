package models.daos.impl;

import models.daos.DAOException;

import com.avaje.ebean.Ebean;

public class EbeanUtils {
    
    public static <T> T findOrException(Class<T> beanType, Object id){
        T found = Ebean.find(beanType, id);
        if (found != null)
            return found;
        else
            throw new DAOException.NoEncontradoException(beanType);
    }

}
