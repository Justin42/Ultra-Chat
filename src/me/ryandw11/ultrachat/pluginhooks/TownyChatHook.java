package me.ryandw11.ultrachat.pluginhooks;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import me.ryandw11.ultrachat.api.ChannelChatEvent;
import me.ryandw11.ultrachat.api.UltraChatAPI;
import me.ryandw11.ultrachat.api.UltraChatHookType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class TownyChatHook {
    @EventHandler
    public void channelChat(ChannelChatEvent e) {
        if(e.getScope().equalsIgnoreCase("town") && UltraChatAPI.getInstance().isHookActive(UltraChatHookType.Towny)) {
            e.getRecipients().addAll(getOnlineTownMembers(e.getPlayer()));
        }
    }

    private List<Player> getOnlineTownMembers(Player player) {
        try {
            Resident resident = TownyUniverse.getDataSource().getResident(player.getName());
            if(resident != null && resident.hasTown()) {
                Town town = resident.getTown();
                return TownyUniverse.getOnlinePlayers(town);
            }
        } catch(NotRegisteredException ex) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }
}
