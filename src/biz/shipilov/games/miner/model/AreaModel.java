package biz.shipilov.games.miner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import biz.shipilov.games.miner.model.AreaModelListener.ChangeCellStateEvent;

public class AreaModel implements SourcesAreaModelEvents {
	
	private List<AreaModelListener> listeners = new LinkedList<AreaModelListener>();
	private final Dimensions dimensions;
	private final Cell[][][] cells;
	
	public AreaModel(Dimensions dimensions) {
		this.dimensions = dimensions;
		cells = new Cell[dimensions.getDepth()][dimensions.getWidth()][dimensions.getHeight()];
		fillMineCells();
		fillEmptyCells();
	}
	
	private void fillMineCells() {
		List<Boolean> mines = new ArrayList<Boolean>(dimensions.getCellsCount());
		for (int i = 0; i < dimensions.getCellsCount(); i++)
			mines.add(i < dimensions.getMinesCount());
		Collections.shuffle(mines);
		
		for (int z = 0; z < dimensions.getDepth(); z++)
			for (int x = 0; x < dimensions.getWidth(); x++)
				for (int y = 0; y < dimensions.getHeight(); y++) {
					if (mines.get(z*(dimensions.getWidth()*dimensions.getHeight()) + x*dimensions.getHeight() + y))
						cells[z][x][y] = new MineCell();
				}
	}

	private void fillEmptyCells() {
		for (int z = 0; z < dimensions.getDepth(); z++)
			for (int x = 0; x < dimensions.getWidth(); x++)
				for (int y = 0; y < dimensions.getHeight(); y++) {
					CellPosition cellPosition = new CellPosition(x, y, z);
					if (getCell(cellPosition) != null) 
						continue;
					cells[z][x][y] = new EmptyCell(getCountMines(AreaUtils.getAdjacentCells(dimensions, cellPosition)));
				}
					
	}
	
	private int getCountMines(List<CellPosition> positions) {
		int result = 0;
		for(CellPosition position : positions)
			if (getCell(position) instanceof MineCell)
				result++;
		return result;
	}

	/**
	 * открыть €чейку с указанными координатами. 
	 * соответствует клику левой кнопкой мыши по закрытой €чейке.
	 * рекурсивно измен€ет состо€ние €чеек.
	 * об€зательно вызовет событие onOpenCells, может вызвать событие onWinning, onBlowUp
	 *  
	 * @param column
	 * @param row
	 * @param depth
	 */
	public void openCell(CellPosition cellPosition) {
		if (!getCell(cellPosition).getState().canBeOpened())
			return;
		getCell(cellPosition).setState(CellState.OPENED);
		fireChangeCellState(new ChangeCellStateEvent(this, cellPosition, CellState.CLOSED));

		if (getCell(cellPosition) instanceof EmptyCell)
			//если €чейка нулева€ - то открываем ее и все закрытые смежные €чейки
			if (((EmptyCell)getCell(cellPosition)).isZero())
				for(CellPosition  position : AreaUtils.getAdjacentCells(dimensions, cellPosition))
					if (getCell(position).getState() == CellState.CLOSED)
						openCell(position);
	}
	
	public void markCell(CellPosition cellPosition) {
		ChangeCellStateEvent event = new ChangeCellStateEvent(this, cellPosition, getCell(cellPosition).getState());
		getCell(cellPosition).setState(getCell(cellPosition).getState().afterMarkedState());
		fireChangeCellState(event);
	}
	
	public Cell getCell(CellPosition position) {
		return dimensions.contains(position) ? getCells()[position.getDepth()][position.getColumn()][position.getRow()] : null;
	}
	
	/**
	 * открывает €чейки, которые логично открыть при соответствующей расстановке флажков.
	 * соответствует клику двум€ кнопками мыши по открытой €чейке с цифрой.
	 * @param column
	 * @param row
	 * @param depth
	 */
	public void openRegion(CellPosition cellPosition) {
		//посчитать количество отмеченных мин вокруг, если оно равно количеству указанному в €чейке то открываем все смежные €чейки
		if (!getCell(cellPosition).canOpenRegion()) 
			return;
		EmptyCell cell = (EmptyCell)getCell(cellPosition);
		int adjacentMarkedCount = 0;
		for(CellPosition position : AreaUtils.getAdjacentCells(dimensions, cellPosition))
			if (getCell(position).getState() == CellState.MARKED)
				adjacentMarkedCount++;
		
		if (adjacentMarkedCount == cell.getMinesCount())
			AreaUtils.handleAdjacentCells(dimensions, cellPosition, new AreaUtils.Handler() {
				public void handle(CellPosition position) {
					openCell(position);
				}
			});
		 
	}

	public AreaSlice getSlice(int depth) {
		if (depth >= getDimensions().depth)
			throw new IllegalArgumentException("depth too long");
		return new AreaSlice(this, depth);
	}
	
	// SourcesAreaModelEvents implementation
	public void addAreaModelListener(AreaModelListener listener) {
		listeners.add(listener);
	}

	public void removeAreaModelListener(AreaModelListener listener) {
		listeners.remove(listener);
	}

	public void fireChangeCellState(ChangeCellStateEvent event) {
		for(AreaModelListener listener : listeners) 
			listener.onChangeCellState(event);
	}

	public Dimensions getDimensions() {
		return dimensions;
	}

	public Cell[][][] getCells() {
		return cells;
	}

	public static class Dimensions implements Serializable {
		private static final long serialVersionUID = 6724821918221242470L;
		private final int height;
		private final int width;
		private final int depth;
		private final int minesCount;
		
		public Dimensions(int width, int height, int depth, int minesCount) {
			if (width < 1 || height < 1 || depth < 1 || width * height * depth < minesCount)
				throw new IllegalArgumentException("illegal dimensions");
			this.width = width;
			this.height = height;
			this.depth = depth;
			this.minesCount = minesCount;
		}

		public int getHeight() {
			return height;
		}

		public int getWidth() {
			return width;
		}

		public int getDepth() {
			return depth;
		}

		public int getMinesCount() {
			return minesCount;
		}
		
		public int getCellsCount() {
			return height * width * depth;
		}
		
		public boolean contains(CellPosition cellPosition) {
			return cellPosition.getColumn() < width 
				&& cellPosition.getRow() < height 
				&& cellPosition.getDepth() < depth;  
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder()
				.append(width)
				.append(height)
				.append(depth)
				.append(minesCount)
				.toHashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof Dimensions))
				return false;
			Dimensions other = (Dimensions)obj;
			
			return other.width == width 
				&& other.height == height
				&& other.depth == depth
				&& other.minesCount == minesCount;
		}
	}
	
	public static class CellPosition {
		private final int column;
		private final int row;
		private final int depth;
		
		public CellPosition(int column, int row, int depth) {
			if (column < 0 || row < 0 || depth < 0)
				throw new IllegalArgumentException("invalid cell position");
			this.column = column;
			this.row = row;
			this.depth = depth;
		}

		public int getColumn() {
			return column;
		}

		public int getRow() {
			return row;
		}

		public int getDepth() {
			return depth;
		}
		
		@Override
		public String toString() {
			return new ToStringBuilder(this)
				.append("column", column)
				.append("row", row)
				.append("depth", depth).toString();
		}
	}
}
