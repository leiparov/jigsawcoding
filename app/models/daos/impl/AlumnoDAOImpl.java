package models.daos.impl;

import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Query;

import models.daos.*;
import models.entities.*;

public class AlumnoDAOImpl implements AlumnoDAO {
    
    public static UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    
    @Override
    public Alumno obtener(int dni){
        return usuarioDAO.obtener(dni, Alumno.class);
    }
    
    @Override
    public List<Alumno> buscarAlumno(String criterio, int max){
        criterio = criterio.toUpperCase();
        Query<Alumno> query = 
                Ebean.find(Alumno.class).where().
                    disjunction().
                        add(Expr.like("upper(nombre)", criterio + '%')).
                        add(Expr.like("upper(nombre)", "% " + criterio + '%')).
                        add(Expr.like("upper(apellido)", criterio + '%')).
                        add(Expr.like("upper(apellido)", "% "+ criterio + '%')).
                    setMaxRows(max);
        return query.findList();
    }
    
    @Override
    public void guardar(Alumno alumno) {
        Ebean.save(alumno);
    }
}
