package lol.gggedr.opencore.providers.ranks;

import lol.gggedr.opencore.api.annotations.ProviderInfo;
import lol.gggedr.opencore.api.providers.ranks.RankProvider;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

@ProviderInfo(pluginName = "LuckPerms")
public class LuckPermsRankProvider implements RankProvider {

    private LuckPerms api;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            this.api = provider.getProvider();
            return;
        }

        throw new RuntimeException("LuckPerms provider not found!");
    }

    @Override
    public String getPlayerRank(String player) {
        User user = this.api.getUserManager().getUser(player);
        if (user == null) {
            return "default";
        }

        return user.getPrimaryGroup();
    }

}
