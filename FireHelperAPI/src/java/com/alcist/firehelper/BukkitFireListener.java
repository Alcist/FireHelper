package com.alcist.firehelper;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by istar on 04/08/14.
 */
public class BukkitFireListener<P extends JavaPlugin> {

    private final Class<P> plugin;

    public BukkitFireListener(Class<P> plugin) {
        this.plugin = plugin;
    }

    public <T> Listener<T> listen(Class<T> dataClass, Callback<T> callback) {
        return new BukkitFireListenerWorker<>(dataClass, callback);
    }

    public interface Listener<T> extends ValueEventListener {}

    private class BukkitFireListenerWorker<T> implements Listener<T> {
        final private Class<T> dataClass;
        final private Callback<T> callback;

        public BukkitFireListenerWorker(Class<T> dataClass, Callback<T> callback) {
            this.dataClass = dataClass;
            this.callback = callback;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Runnable rn = new SyncedT(dataSnapshot);
            Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(plugin), rn);
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Runnable rn = new SyncedT(null);
            Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(plugin), rn);
        }

        private class SyncedT implements Runnable {

            DataSnapshot dataSnapshot;

            public SyncedT(DataSnapshot dataSnapshot) {
                this.dataSnapshot = dataSnapshot;
            }

            @Override
            public void run() {
                if(dataSnapshot != null) {
                    T data = dataSnapshot.getValue(dataClass);
                    callback.onCallBack(data);
                }
                else {
                    callback.onCallBack(null);
                }
            }
        }
    }
}
