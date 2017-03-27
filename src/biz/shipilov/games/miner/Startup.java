package biz.shipilov.games.miner;

import biz.shipilov.games.miner.model.GameSession;
import biz.shipilov.games.miner.view.GameFrame;
import biz.shipilov.games.miner.view.GameView;

public class Startup {
	private static GameView gameView;
	
	public static void main(String[] argv) throws InterruptedException {
		GameSession gameSession = new GameSession();
		GameFrame gameFrame	= new GameFrame(gameSession);
		gameSession.startNewGame(GameSession.BEGINNER_LEVEL);
		gameFrame.setVisible(true);
	}
	
}
