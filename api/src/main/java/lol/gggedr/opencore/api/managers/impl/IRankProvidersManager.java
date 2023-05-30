package lol.gggedr.opencore.api.managers.impl;

import lol.gggedr.opencore.api.managers.Manager;
import lol.gggedr.opencore.api.providers.ranks.RankProvider;

public interface IRankProvidersManager extends Manager {

    void registerProvider(RankProvider provider);

    boolean setProvider(RankProvider provider);

    RankProvider getProvider();

}
