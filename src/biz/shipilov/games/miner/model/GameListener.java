package biz.shipilov.games.miner.model;

public interface GameListener {
	void onLoss();
	void onWinning(int seconds);
	void onTick(int seconds);
	void onMarkedCellsCountChange(int markedCellsCount);
}

class GameListenerAdapter implements GameListener {
	public void onLoss() {}
	public void onMarkedCellsCountChange(int markedCellsCount) {}
	public void onTick(int seconds) {}
	public void onWinning(int seconds) {}
}