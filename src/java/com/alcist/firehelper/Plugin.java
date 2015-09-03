package com.alcist.firehelper;

import com.firebase.client.Firebase;
import com.alcist.firehelper.FireHelper;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by istar on 04/08/14.
 */
public class Plugin extends JavaPlugin implements FireHelper {

    Firebase firebase;

    @Override
    public void onEnable() {
        super.onEnable();
        setConfig();
        String url = getConfig().getString("url");
        if(url.equals("https://default.firebaseio.com")) {
            getLogger().info("You have to change the firebase base url in the config.");
            getPluginLoader().disablePlugin(this);
        }
        getLogger().info("Firebase url: " + url);
        firebase = new Firebase(url);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        firebase = null;
    }

    public void setConfig() {
        saveResource("config.yml", false);
    }

    @Override
    public Firebase getFirebase() {
        return firebase;
    }
}
