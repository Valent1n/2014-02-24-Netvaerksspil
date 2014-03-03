package game;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameServer {
	Map<PortIP, Player> players;
	
	public Set<Player> getPlayers() {
		Set<Player> copy;
		synchronized (players) {
			copy = new HashSet<>(players.values());
		}
		return copy;
	}
	
	public Player getPlayer(InetAddress ip, int port) {
		PortIP soc = new PortIP(ip, port);
		Player out;
		synchronized (players) {
			out = players.get(soc);
		}
		return out;
	}
	
	public Player createPlayer(String playerName, InetAddress address, int port) {
		//TODO 
		return null;
	}
}
