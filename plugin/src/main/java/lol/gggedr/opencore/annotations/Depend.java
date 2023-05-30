package lol.gggedr.opencore.annotations;

import lol.gggedr.opencore.api.managers.Manager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Depend {

    public Class<? extends Manager>[] value();

}
