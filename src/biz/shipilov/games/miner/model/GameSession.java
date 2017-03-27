package biz.shipilov.games.miner.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import biz.shipilov.games.miner.model.AreaModel.Dimensions;
import biz.shipilov.games.miner.model.Championship.Champion;
import biz.shipilov.games.miner.model.GameSessionListener.GameSessionEvent;

public class GameSession implements GameSessionEventsSource {
	
	public static final GameLevel BEGINNER_LEVEL = new GameLevel("Новичок", "новичков", new Dimensions(10, 10, 3, 25));
	
	public static final List<GameLevel> LEVELS = Arrays.asList(
			BEGINNER_LEVEL,
			new GameLevel("Дилетант", "дилетантов", new Dimensions(10, 10, 10, 70)),
			new GameLevel("Любитель", "любителей", new Dimensions(15, 25, 15, 450)),
			new GameLevel("Профессионал", "профессионалов", new Dimensions(20, 35, 20, 1100)));
	
	private Game currentGame;;
	private GameLevel currentGameLevel;
	private Championship championship = new Championship(LEVELS);
	
	private List<GameSessionListener> listeners = new LinkedList<GameSessionListener>();
	
	public GameSession() {
	}
	
	public void startNewGame(GameLevel level) {
		setCurrentGameLevel(level);
		currentGame = new Game(level.getDimensions());
		currentGame.addGameListener(new GameListenerAdapter() {
			@Override
			public void onWinning(int seconds) {
				if(seconds < championship.getChampions().get(currentGameLevel).getResult())
					fireNewChampion(new GameSessionEvent(currentGame, GameSession.this));
			}
		});
		fireNewGame(new GameSessionEvent(currentGame, this));
	}
	
	public void startNewGame() {
		startNewGame(getCurrentGameLevel());
	}
	
	public Championship getChampionship() {
		return championship;
	}
	
	public void inputChampionName(String newChampionName) {
		championship.registerChampion(currentGameLevel, new Champion(newChampionName, currentGame.getSeconds()));
	}
	
	public static class GameLevel implements Comparable<GameLevel>, Serializable {
		private static final long serialVersionUID = -1248579473400716055L;
		private final String name;
		private final String nameForWinnerDialog;
		private final Dimensions dimensions;
		
		public GameLevel(String name, String nameForWinnerDialog, Dimensions dimensions) {
			this.name = name;
			this.nameForWinnerDialog = nameForWinnerDialog;
			this.dimensions = dimensions;
		}

		public String getName() {
			return name;
		}

		public Dimensions getDimensions() {
			return dimensions;
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder()
				.append(name)
				.append(dimensions)
				.toHashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof GameLevel))
				return false;
			GameLevel other = (GameLevel)obj;
			return new EqualsBuilder()
				.append(name, other.name)
				.append(dimensions, other.dimensions)
				.isEquals();
		}

		public int compareTo(GameLevel o) {
			return dimensions.getCellsCount() - o.dimensions.getCellsCount();
		}

		public String getNameForWinnerDialog() {
			return nameForWinnerDialog;
		}
	}

	public void fireNewGame(GameSessionEvent event) {
		for(GameSessionListener listener : listeners)
			listener.onNewGame(event);
	}

	public void fireNewChampion(GameSessionEvent event) {
		for(GameSessionListener listener : listeners)
			listener.onNewChampion(event);
	}
	
	public void addListener(GameSessionListener listener) {
		listeners.add(listener);
	}

	public void removeListener(GameSessionListener listener) {
		listeners.remove(listener);
	}

	public void setCurrentGameLevel(GameLevel currentGameLevel) {
		this.currentGameLevel = currentGameLevel;
	}

	public GameLevel getCurrentGameLevel() {
		return currentGameLevel;
	}

}
