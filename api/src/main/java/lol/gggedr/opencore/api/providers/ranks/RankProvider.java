package lol.gggedr.opencore.api.providers.ranks;

public interface RankProvider {

    default void onEnable() {

    }

    String getPlayerRank(String player);

}
