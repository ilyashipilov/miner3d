package biz.shipilov.games.miner.model;

import biz.shipilov.games.miner.model.AreaModel.CellPosition;

/**
 * ���������, ������� ��������� ������� �������� ��� �������������� ���������� � ����� ����������
 */
public interface AreaModelListener {
	
	public static class AreaModelEvent {
		private final AreaModel model;

		public AreaModelEvent(AreaModel model) {
			this.model = model;
		}

		public AreaModel getModel() {
			return model;
		}
		
	}

	public static class ChangeCellStateEvent extends AreaModelEvent {
		private final CellState previousCellState;
		private final CellPosition cellPosition;

		public ChangeCellStateEvent(AreaModel model, CellPosition cellPosition, CellState previousCellState) {
			super(model);
			this.cellPosition = cellPosition;
			this.previousCellState = previousCellState;
		}

		public CellPosition getCellPosition() {
			return cellPosition;
		}

		public CellState getPreviousCellState() {
			return previousCellState;
		}

	}
	
	/**
	 * ������� 
	 */
	public void onChangeCellState(ChangeCellStateEvent event);
}
