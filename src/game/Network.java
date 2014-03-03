package game;

import javax.sql.rowset.serial.SerialArray;

public class Network {
	
	private Network singleton;
	private GamePlayer gamePlayer;
	
	private Network() {
		singleton = new Network();
	}

	public Network getNetwork() {
		if(singleton != null) singleton = new Network();
		return singleton;
	}

	public GamePlayer getGamePlayer() {
		return gamePlayer;
	}

	public void setGamePlayer(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}
	
	
	
	

	
	
	
	
	

}
