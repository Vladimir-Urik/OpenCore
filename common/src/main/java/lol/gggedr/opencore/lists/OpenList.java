package lol.gggedr.opencore.lists;

import java.util.ArrayList;
import java.util.Collections;

public class OpenList<T> extends ArrayList<T> {

    @SafeVarargs
    public static <T> OpenList<T> of(T... elements) {
        OpenList<T> list = new OpenList<>();
        Collections.addAll(list, elements);
        return list;
    }

}
