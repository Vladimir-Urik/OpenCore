package lol.gggedr.opencore.api.cons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenPlayer {

    private final String name;
    private HashMap<String, Long> economy = new HashMap<>();
    private List<String> loadedData = new ArrayList<>();

    public OpenPlayer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Long> getEconomy() {
        return economy;
    }

    public void setEconomy(HashMap<String, Long> economy) {
        this.economy = economy;
    }

    public List<String> getLoadedData() {
        return loadedData;
    }

    public void setLoadedData(List<String> loadedData) {
        this.loadedData = loadedData;
    }

    public void addLoadedData(String data) {
        this.loadedData.add(data);
    }

    public void removeLoadedData(String data) {
        this.loadedData.remove(data);
    }

    public boolean isLoaded(String data) {
        return this.loadedData.contains(data);
    }
}
