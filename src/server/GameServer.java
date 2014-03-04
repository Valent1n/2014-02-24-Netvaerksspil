package server;

import game.Direction;
import game.Player;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sun.xml.internal.ws.api.pipe.NextAction;

public class GameServer {
	private Map<PortIP, ServerPlayer> players;
	private Set<String> playerNames;
	private int nexPlayerId = 0;
	
	//TODO  constructor
	
	
	public Set<ServerPlayer> getPlayers() {
		Set<ServerPlayer> copy;
		synchronized (players) {
			copy = new HashSet<>(players.values());
		}
		return copy;
	}
	
	public ServerPlayer getPlayer(InetAddress ip, int port) {
		PortIP soc = new PortIP(ip, port);
		ServerPlayer out;
		synchronized (players) {
			out = players.get(soc);
		}
		return out;
	}
	
	public void movePlayer(ServerPlayer player, Direction direction) {
		//TODO
		
	}
	
	public ServerPlayer createPlayer(String playerName, InetAddress address, int port) {
		ServerPlayer out = null;
		if (!playerNames.contains(playerName)) {
			PortIP pip = new PortIP(address, port);
			if (players.containsKey(pip)) {
				out = players.get(pip);
			} else {
				out = new ServerPlayer(playerName, nexPlayerId, address, port);
				nexPlayerId++;
			}
		}
		return out;
	}
}
