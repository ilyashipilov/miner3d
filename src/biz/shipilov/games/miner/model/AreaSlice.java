package biz.shipilov.games.miner.model;

public class AreaSlice {
	private final AreaModel areaModel;
	private final int depth;

	public AreaSlice(AreaModel areaModel, int depth) {
		this.areaModel = areaModel;
		this.depth = depth;
	}
	
	public Cell[][] getCells() {
		return areaModel.getCells()[this.depth];
	}
	
	public int getHeight() {
		return areaModel.getDimensions().getHeight();		
	}
	
	public int getWidth() {
		return areaModel.getDimensions().getWidth();
	}
}
