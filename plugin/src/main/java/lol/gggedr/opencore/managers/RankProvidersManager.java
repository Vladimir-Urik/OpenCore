package lol.gggedr.opencore.managers;

import lol.gggedr.opencore.api.annotations.ProviderInfo;
import lol.gggedr.opencore.api.managers.Manager;
import lol.gggedr.opencore.api.managers.impl.IRankProvidersManager;
import lol.gggedr.opencore.api.providers.ranks.RankProvider;
import lol.gggedr.opencore.lists.OpenList;
import lol.gggedr.opencore.providers.ranks.DefaultRankProvider;
import org.bukkit.Bukkit;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class RankProvidersManager implements IRankProvidersManager {

    private final OpenList<RankProvider> providers = new OpenList<>();
    private RankProvider provider;

    @Override
    public void onEnable() {
        Reflections reflections = new Reflections("lol.gggedr.opencore.providers.ranks", Scanners.SubTypes);
        reflections.getSubTypesOf(RankProvider.class).forEach(provider -> {
            try {
                String requiredPlugin = provider.getAnnotation(ProviderInfo.class).pluginName();
                if(requiredPlugin.length() > 0 && Bukkit.getPluginManager().getPlugin(requiredPlugin) == null) {
                    return;
                }

                this.registerProvider(provider.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        if(provider != null) {
            provider.onEnable();
            return;
        }

        OpenList<RankProvider> newProviders = providers.stream()
                .filter(provider -> !provider.getClass().isInstance(new DefaultRankProvider()))
                .collect(OpenList::new, OpenList::add, OpenList::addAll);

        for (RankProvider provider : newProviders) {
            boolean success = setProvider(provider);
            if (success) {
                break;
            }

            System.out.println("Failed to load provider: " + provider.getClass().getSimpleName());
        }

        if(provider == null) {
            setProvider(new DefaultRankProvider());
        }
    }

    @Override
    public void registerProvider(RankProvider provider) {
        providers.add(provider);
    }

    @Override
    public boolean setProvider(RankProvider provider) {
        try {
            provider.onEnable();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        this.provider = provider;
        return true;
    }

    @Override
    public RankProvider getProvider() {
        return provider;
    }
}
