package biz.shipilov.games.miner.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.commons.lang.builder.ToStringBuilder;

import biz.shipilov.games.miner.model.AreaSlice;
import biz.shipilov.games.miner.model.Cell;

/**
 * Панель отображающая срез игрового поля   
 * @author ilya
 * 
 */
public class SlicePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final static int CELL_HEIGHT = 16;
	private final static int CELL_WIDTH = 16;

	private Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	private List<SliceListener> listeners = new LinkedList<SliceListener>();
	private AreaSlice areaSlice;
	private Cell2D highlightedArea;
	private Cell2D targetCell;
	
	private boolean gameOverMode;

	public SlicePanel(AreaSlice areaSlice) {
		this.areaSlice = areaSlice;
		addMouseMotionListener(new MouseMotionAdapter() {
			
			public void mouseMoved(MouseEvent e) {
				Cell2D newCell = new Cell2D(e.getX() / CELL_WIDTH,  e.getY() / CELL_HEIGHT);
				if (!newCell.equals(getTargetCell()))
					targetCell = newCell;
				fireMouseOver();
			}
			
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				fireClick(new ClickEvent(SlicePanel.this, e.getY() / CELL_HEIGHT, e.getX() / CELL_WIDTH, MouseButton.fromMouseEvent(e)));
			}
		});
		setHighlightedArea(new Cell2D(4, 4));
		//setSize(new Dimension(areaSlice.getWidth() * CELL_WIDTH + 1, areaSlice.getHeight() * CELL_HEIGHT + 1));
		setPreferredSize(new Dimension(areaSlice.getWidth() * CELL_WIDTH + 1, areaSlice.getHeight() * CELL_HEIGHT + 1));
	}

	public void setAreaSlice(AreaSlice areaSlice) {
		this.areaSlice = areaSlice;
		setPreferredSize(new Dimension(areaSlice.getWidth() * CELL_WIDTH + 1, areaSlice.getHeight() * CELL_HEIGHT + 1));
	}

	public static class Cell2D {
		private final int column;
		private final int row;

		public Cell2D(int column, int row) {
			this.column = column;
			this.row = row;
		}

		public int getColumn() {
			return column;
		}

		public int getRow() {
			return row;
		}
		
		@Override
		public boolean equals(Object obj) {
			Cell2D other = (Cell2D)obj;
			if (other == null) return false;
			return other.column == column && other.row == row;
		}
		
		@Override
		public String toString() {
			return new ToStringBuilder(this)
				.append("column", column)
				.append("row", row)
				.toString();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			g.setClip(0, 0, areaSlice.getWidth() * CELL_WIDTH + 1, areaSlice.getHeight() * CELL_HEIGHT + 1);
			Cell[][] cells = areaSlice.getCells();
			for (int column = 0; column < cells.length; column++)
				for (int row = 0; row < cells[column].length; row++)
					g.drawImage(getCellImage("/biz/shipilov/games/miner/view/resources/" + cells[column][row].getResourceName(gameOverMode) + ".png"), column * CELL_WIDTH, row * CELL_HEIGHT, null);

			g.setColor(new Color(165, 165, 165));
			g.drawLine(0, areaSlice.getHeight() * CELL_HEIGHT, areaSlice.getWidth() * CELL_WIDTH, areaSlice.getHeight() * CELL_HEIGHT);
			g.drawLine(areaSlice.getWidth() * CELL_WIDTH, 0, areaSlice.getWidth() * CELL_WIDTH, areaSlice.getHeight() * CELL_HEIGHT);

			if (getHighlightedArea() != null) {
				g.setColor(new Color(0, 0, 0));
				((Graphics2D)g).setStroke(new BasicStroke(
			    	      1f, 
			    	      BasicStroke.CAP_ROUND, 
			    	      BasicStroke.JOIN_ROUND, 
			    	      1f, 
			    	      new float[] {2f}, 
			    	      0f));
				g.drawRect((getHighlightedArea().getColumn()-1) * CELL_WIDTH, (getHighlightedArea().getRow()-1) * CELL_HEIGHT, CELL_WIDTH * 3, CELL_HEIGHT * 3);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private BufferedImage getCellImage(String name) throws IOException {
		if (!images.containsKey(name))
			images.put(name, ImageIO.read(SlicePanel.class.getResourceAsStream(name))); 
		return images.get(name);
	}

	public void addClickListener(SliceListener listener) {
		listeners.add(listener);
	}
	
	private void fireClick(ClickEvent event) {
		for(SliceListener listener : listeners)
			listener.onClick(event);
	}

	private void fireMouseOver() {
		for(SliceListener listener : listeners)
			listener.onMouseOver(this);
	}

	public void setHighlightedArea(Cell2D highlightedArea) {
		this.highlightedArea = highlightedArea;
		repaint();
	}

	public Cell2D getHighlightedArea() {
		return highlightedArea;
	}

	public Cell2D getTargetCell() {
		return targetCell;
	}

	public void setGameOverMode(boolean gameOverMode) {
		this.gameOverMode = gameOverMode;
	}

	public boolean isGameOverMode() {
		return gameOverMode;
	}

	public static enum MouseButton {
		LEFT, RIGHT, BOTH;
		
		static MouseButton fromMouseEvent(MouseEvent event) {
			if ((event.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK 
					&& (event.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) 
				return BOTH;
			if (event.getButton() == MouseEvent.BUTTON1)
				return LEFT;
			return RIGHT;
		}
	}
	
	public static class ClickEvent {
		private final int row;
		private final int column;
		private final MouseButton mouseButton;
		private final SlicePanel source;
		
		public ClickEvent(SlicePanel source, int row, int column, MouseButton mouseButton) {
			this.source = source;
			this.row = row;
			this.column = column;
			this.mouseButton = mouseButton;
		}
		
		public int getRow() {
			return row;
		}
		
		public int getColumn() {
			return column;
		}
		
		public MouseButton getMouseButton() {
			return mouseButton;
		}
		
		@Override
		public String toString() {
			return new ToStringBuilder(this)
				.append("row", row)
				.append("column", column)
				.append("mouseButton", mouseButton).toString();
		}

		public SlicePanel getSource() {
			return source;
		}
		
	}
	
	public interface SliceListener {
		public void onClick(ClickEvent event);
		public void onMouseOver(SlicePanel source);
	}
}
