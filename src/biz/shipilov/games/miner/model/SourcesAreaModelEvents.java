package biz.shipilov.games.miner.model;

import biz.shipilov.games.miner.model.AreaModelListener.ChangeCellStateEvent;

public interface SourcesAreaModelEvents {
    
	void addAreaModelListener(AreaModelListener listener);
    void removeAreaModelListener(AreaModelListener listener);
    void fireChangeCellState(ChangeCellStateEvent event);
    
}
