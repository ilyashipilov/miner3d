package biz.shipilov.games.miner.model;

public class EmptyCell extends Cell {
	private final int minesCount;
	
	public EmptyCell(int minesCount) {
		this.minesCount = minesCount;
	}

	public int getMinesCount() {
		return minesCount;
	}
	
	public boolean isZero() {
		return getMinesCount() == 0;
	}

	@Override
	//TODO
	public String getResourceName(boolean gameOver) {
		if (state == CellState.CLOSED)
			return "closed";
		if (state == CellState.MARKED)
			return gameOver ? "mine-error" : "marked";
		if (state == CellState.OPENED)
			return "cell" + getMinesCount();
		throw new IllegalStateException("not implemented yet");
	}
	
	@Override
	public boolean canOpenRegion() {
		return state.canOpenRegion() && !isZero();
	}
	
}
