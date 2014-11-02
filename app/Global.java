import java.util.List;

import models.daos.UsuarioDAO;
import models.entities.Usuario;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import com.avaje.ebean.Ebean;


public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        
        UsuarioDAO udao = new UsuarioDAO();
        
        // Check if the database is empty
        if (Ebean.find(Usuario.class).findList().size() == 0) {
            List<?> lista = (List<?>) Yaml.load("bootstrap-data.yml");
            for(Object o : lista){
                Ebean.save(o);
            }
            udao.cambiarPassword(45407679, "1234");
            udao.cambiarPassword(47614910, "1234");
        }
    }
    
}
