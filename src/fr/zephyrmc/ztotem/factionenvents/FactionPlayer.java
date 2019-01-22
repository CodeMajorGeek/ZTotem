package fr.zephyrmc.ztotem.factionenvents;

import org.bukkit.entity.*;

import com.massivecraft.factions.entity.*;

public class FactionPlayer {
	
	private Faction faction = null;
	private static MPlayer mplayer = null;
	
	public FactionPlayer(Player player) {
		
		mplayer = MPlayer.get(player);
		faction = mplayer.getFaction();
	}
	
	public String getFactionName() {
		
		return faction.getName();
	}
}
