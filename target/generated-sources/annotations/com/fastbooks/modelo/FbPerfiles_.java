package com.fastbooks.modelo;

import com.fastbooks.modelo.FbModuloXPerfil;
import com.fastbooks.modelo.FbPerfilXUsuario;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-04-04T10:56:50")
@StaticMetamodel(FbPerfiles.class)
public class FbPerfiles_ { 

    public static volatile SingularAttribute<FbPerfiles, String> descripcion;
    public static volatile SingularAttribute<FbPerfiles, String> estado;
    public static volatile SingularAttribute<FbPerfiles, BigDecimal> idPerfil;
    public static volatile ListAttribute<FbPerfiles, FbPerfilXUsuario> fbPerfilXUsuarioList;
    public static volatile ListAttribute<FbPerfiles, FbModuloXPerfil> fbModuloXPerfilList;
    public static volatile SingularAttribute<FbPerfiles, Date> fechaCreacion;
    public static volatile SingularAttribute<FbPerfiles, String> nombre;

}