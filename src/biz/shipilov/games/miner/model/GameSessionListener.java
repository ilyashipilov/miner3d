package biz.shipilov.games.miner.model;


public interface GameSessionListener {
	void onNewGame(GameSessionEvent event);
	void onNewChampion(GameSessionEvent event);
	
	public static class GameSessionEvent {
		private final Game game;
		private final GameSession session;
		
		public GameSessionEvent(Game game, GameSession session) {
			this.game = game;
			this.session = session;
		}
		
		public Game getGame() {
			return game;
		}

		public GameSession getSession() {
			return session;
		}

	}

}

interface GameSessionEventsSource {
	void addListener(GameSessionListener listener);
	void removeListener(GameSessionListener listener);
	void fireNewGame(GameSessionListener.GameSessionEvent event);
	void fireNewChampion(GameSessionListener.GameSessionEvent event);
}