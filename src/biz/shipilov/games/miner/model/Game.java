package biz.shipilov.games.miner.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import biz.shipilov.games.miner.model.AreaModel.CellPosition;
import biz.shipilov.games.miner.model.AreaModel.Dimensions;

public class Game implements SourcesGameEvents {
	private final AreaModel areaModel;
	private List<GameListener> listeners = new LinkedList<GameListener>();
	private final GameState readyState = new ReadyState();
	private final GameState activeState = new ActiveState();
	private final GameState gameOverState = new GameOverState();
	private GameState currentState;
	private Timer timer;
	private int seconds = 1;
	private int openedCellsCount = 0;
	private int markedCellsCount = 0;

	
	public Game(Dimensions dimensions) {
		areaModel = new AreaModel(dimensions);
		areaModel.addAreaModelListener(new AreaModelListener() {

			public void onChangeCellState(ChangeCellStateEvent event) {
				Cell cell = areaModel.getCell(event.getCellPosition());
				//взорвались
				if (cell instanceof MineCell && cell.getState() == CellState.OPENED) {
					timer.cancel();
					fireLoss();
					currentState = gameOverState;
					return;
				}
				
				if (event.getPreviousCellState() != CellState.OPENED 
						&& cell.getState() == CellState.OPENED)
					openedCellsCount++;
				
				if (cell.getState() == CellState.MARKED) {
					markedCellsCount++;
					fireMarkedCellsCountChange(markedCellsCount);
				}
				
				if (cell.getState() == CellState.CLOSED && event.getPreviousCellState() == CellState.MARKED) {
					markedCellsCount--;
					fireMarkedCellsCountChange(markedCellsCount);
				}

				//если количество отмеченных мин + количество открытых ячеек = общему объему игрового пространства,
				if (openedCellsCount + markedCellsCount == areaModel.getDimensions().getCellsCount()
						&& markedCellsCount == areaModel.getDimensions().getMinesCount()) {

					timer.cancel();
					fireWinning(seconds);
					currentState = gameOverState;
				}
				
			}
			
		});
		currentState = readyState;
	}
	
	public void openCell(CellPosition cellPosition) {
		currentState.openCell(cellPosition);
	}
	
	public void markCell(CellPosition cellPosition) {
		currentState.markCell(cellPosition);
	}
	
	public void openRegion(CellPosition cellPosition) {
		currentState.openRegion(cellPosition);
	}
	
	public AreaModel getAreaModel() {
		return areaModel;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	//состояние игры
	interface GameState {
		void openCell(CellPosition cellPosition);
		void markCell(CellPosition cellPosition);
		void openRegion(CellPosition cellPosition);
	}

	//ожидание первого клика
	class ReadyState implements GameState {

		private void start() {
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					fireTick(seconds++);
				}
				
			}, 1000, 1000);
			currentState = activeState;
		}

		public void markCell(CellPosition cellPosition) {
			start();
			Game.this.markCell(cellPosition);
		}

		public void openCell(CellPosition cellPosition) {
			start();
			Game.this.openCell(cellPosition);
		}

		public void openRegion(CellPosition cellPosition) {
			throw new IllegalStateException("no regions!");
		}
		
	}
	
	//игра
	class ActiveState implements GameState {

		public void markCell(CellPosition cellPosition) {
			areaModel.markCell(cellPosition);
		}

		public void openCell(CellPosition cellPosition) {
			areaModel.openCell(cellPosition);
		}

		public void openRegion(CellPosition cellPosition) {
			areaModel.openRegion(cellPosition);
		}
		
	}

	//игра окончена
	class GameOverState implements GameState {

		public void markCell(CellPosition cellPosition) { }

		public void openCell(CellPosition cellPosition) { }

		public void openRegion(CellPosition cellPosition) { }
		
	}
	
	//SourcesGameEvents implements 
	public void addGameListener(GameListener listener) {
		listeners.add(0, listener);
	}

	public void removeGameListener(GameListener listener) {
		listeners.remove(listener);
	}
	
	public void fireLoss() {
		for(GameListener listener : listeners)
			listener.onLoss();
	}

	public void fireWinning(int seconds) {
		for(GameListener listener : listeners)
			listener.onWinning(seconds);
	}

	public void fireTick(int seconds) {
		for(GameListener listener : listeners)
			listener.onTick(seconds);
	}

	public void fireMarkedCellsCountChange(int markedCellsCount) {
		for(GameListener listener : listeners)
			listener.onMarkedCellsCountChange(markedCellsCount);
	}

}
