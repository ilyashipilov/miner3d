package biz.shipilov.games.miner.model;

public enum CellState {
	CLOSED {
		@Override
		public boolean canBeOpened() {
			return true;
		}

		@Override
		public CellState afterMarkedState() {
			return MARKED;
		}

		@Override
		public boolean canOpenRegion() {
			return false;
		}
	}, MARKED {
		@Override
		public boolean canBeOpened() {
			return false;
		}

		@Override
		public CellState afterMarkedState() {
			return CLOSED;
		}

		@Override
		public boolean canOpenRegion() {
			return false;
		}
	}, OPENED {
		@Override
		public boolean canBeOpened() {
			return false;
		}

		@Override
		public CellState afterMarkedState() {
			return OPENED;
		}

		@Override
		public boolean canOpenRegion() {
			return true;
		}
	};
	public abstract boolean canBeOpened();
	public abstract boolean canOpenRegion();
	public abstract CellState afterMarkedState();
}
