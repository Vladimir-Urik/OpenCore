package lol.gggedr.opencore.api.actions;

import lol.gggedr.opencore.api.cons.ActionCtx;

import java.util.ArrayList;
import java.util.List;

public interface Action {

    String getIdentifier();

    default List<String> getAliases() {
        return new ArrayList<>();
    }

    void execute(ActionCtx<?> ctx);

}
