package biz.shipilov.games.miner.model;

public abstract class Cell {
	
	protected CellState state = CellState.CLOSED;

	public void setState(CellState state) {
		this.state = state;
	}

	public CellState getState() {
		return state;
	}
	
	public abstract String getResourceName(boolean gameOver);
	public abstract boolean canOpenRegion();
}
