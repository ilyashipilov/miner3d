package biz.shipilov.games.miner.model;

import java.util.LinkedList;
import java.util.List;

import biz.shipilov.games.miner.model.AreaModel.CellPosition;
import biz.shipilov.games.miner.model.AreaModel.Dimensions;

public class AreaUtils {
	public static List<CellPosition> getAdjacentCells(Dimensions dimensions, CellPosition cellPosition) {
		List<CellPosition> result = new LinkedList<CellPosition>();
		for (int z = cellPosition.getDepth() - 1; z <= cellPosition.getDepth() + 1; z++)
			for (int y = cellPosition.getRow() - 1; y <= cellPosition.getRow() + 1; y++)
				for (int x = cellPosition.getColumn() - 1; x <= cellPosition.getColumn() + 1; x++)
					if (z >= 0 && z < dimensions.getDepth() && y >= 0 && y < dimensions.getHeight() && x >= 0 && x < dimensions.getWidth())
						result.add(new CellPosition(x, y, z));
		return result;
	}
	
	public static void handleAdjacentCells(Dimensions dimensions, CellPosition cellPosition, Handler handler) {
		for(CellPosition position : getAdjacentCells(dimensions, cellPosition))
			handler.handle(position);
	}
	
	public interface Handler {
		void handle(CellPosition position);
	}
}
