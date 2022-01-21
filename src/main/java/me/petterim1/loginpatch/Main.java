package me.petterim1.loginpatch;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.plugin.PluginBase;

import java.util.List;

public class Main extends PluginBase implements Listener {

    private List<String> trustedAddresses;
    private boolean opBypass;
    private boolean allowLocal;
    private boolean allowLocalhost;
    private String blockedMessage;

    public void onEnable() {
        saveDefaultConfig();
        trustedAddresses = getConfig().getStringList("trustedAddresses");
        opBypass = getConfig().getBoolean("opBypass");
        allowLocal = getConfig().getBoolean("allowLocal");
        allowLocalhost = getConfig().getBoolean("allowLocalhost");
        blockedMessage = getConfig().getString("blockedMessage").replace("&n", "\n");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent e) {
        String address = e.getPlayer().getLoginChainData().getServerAddress().toLowerCase().split(":")[0];
        if (!trustedAddresses.contains(address) && !((opBypass && e.getPlayer().isOp()) || (allowLocal && address.startsWith("192.168.")) || (allowLocalhost && (address.equals("127.0.0.1") || address.equals("localhost"))))) {
            e.setKickMessage(blockedMessage.replace("%address%", address));
            e.setCancelled(true);
        }
    }
}
