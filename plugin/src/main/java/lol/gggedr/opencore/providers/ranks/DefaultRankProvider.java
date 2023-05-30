package lol.gggedr.opencore.providers.ranks;

import lol.gggedr.opencore.api.annotations.ProviderInfo;
import lol.gggedr.opencore.api.providers.ranks.RankProvider;

@ProviderInfo(pluginName = "")
public class DefaultRankProvider implements RankProvider {

    @Override
    public String getPlayerRank(String player) {
        return "default";
    }

}
