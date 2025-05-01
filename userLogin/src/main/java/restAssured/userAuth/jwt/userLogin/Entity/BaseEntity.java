package restAssured.userAuth.jwt.userLogin.Entity;

import java.io.Serializable;

public abstract class BaseEntity<K extends Serializable> {
    public abstract K getId();

}

