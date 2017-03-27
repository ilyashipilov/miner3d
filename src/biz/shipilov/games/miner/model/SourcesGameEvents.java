package biz.shipilov.games.miner.model;

public interface SourcesGameEvents {
    void addGameListener(GameListener listener);
    void removeGameListener(GameListener listener);
    void fireLoss();
    void fireWinning(int seconds);
    void fireTick(int seconds);
    void fireMarkedCellsCountChange(int markedCellsCount);
}
