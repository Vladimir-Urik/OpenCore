package lol.gggedr.opencore.api.modules.economy;

import lol.gggedr.opencore.api.modules.Module;
import lol.gggedr.opencore.api.modules.economy.cons.Economy;

import java.util.HashMap;

public interface IEconomyModule extends Module {

    public Economy getEconomy(String name);

    public void addEconomy(String name, String format, long defaultValue, long min, long max);

    public void removeEconomy(String name);

    public void addAmount(String name, String player, long amount);

    public void setAmount(String name, String player, long amount);

    public void removeAmount(String name, String player, long amount);

    public long getAmount(String name, String player);

    public HashMap<String, Long> getAmounts(String player);

}
