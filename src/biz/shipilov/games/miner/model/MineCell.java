package biz.shipilov.games.miner.model;

public class MineCell extends Cell {

	@Override
	//TODO
	public String getResourceName(boolean gameOver) {
		if (state == CellState.CLOSED)
			return gameOver ? "mine" : "closed";
		if (state == CellState.MARKED)
			return "marked";
		if (state == CellState.OPENED)
			return "mine-blowup";
		throw new IllegalStateException("not implemented yet");
	}
	//TODO

	@Override
	public boolean canOpenRegion() {
		return false;
	}

}
