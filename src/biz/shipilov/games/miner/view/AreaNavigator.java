package biz.shipilov.games.miner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.commons.lang.builder.ToStringBuilder;

import biz.shipilov.games.miner.model.AreaModel;
import biz.shipilov.games.miner.model.AreaModelListener;
import biz.shipilov.games.miner.model.AreaModel.CellPosition;
import biz.shipilov.games.miner.view.SlicePanel.ClickEvent;
import biz.shipilov.games.miner.view.SlicePanel.MouseButton;
import biz.shipilov.games.miner.view.SlicePanel.SliceListener;

/**
 * Три среза игрового поля
 * @author ilya
 *
 */
public class AreaNavigator extends JPanel implements AreaModelListener, SliceListener  {
	private static final int DEPTH_NAVIGATION_BUTTON_WIDTH = 16;
	private static final long serialVersionUID = 1L;
	private final AreaModel areaModel;
	private final int visibledSlices;
	private int firstSiceDepth;
	private List<SlicePanel> slicePanels = new ArrayList<SlicePanel>();
	private List<AreaListener> listeners = new LinkedList<AreaListener>();
	private final JButton buttonLeft;
	private final JButton buttonRight;

	public AreaNavigator(AreaModel areaModel, int visibledSlices) {
		this.areaModel = areaModel;
		this.visibledSlices = visibledSlices;
		firstSiceDepth = 0;

		setBackground(new Color(255, 255, 255 ));
		
		buttonLeft = new JButton();
		buttonLeft.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setFirstSiceDepth(firstSiceDepth - 1);
			}
			
		});
		buttonLeft.setEnabled(false);
		add(buttonLeft, BorderLayout.WEST);
		buttonLeft.setIcon(new NavigatorIcon("/biz/shipilov/games/miner/view/resources/navigator.left.png"));

		
		for (int sliceDepth = firstSiceDepth; sliceDepth < firstSiceDepth + visibledSlices; sliceDepth++) {
			SlicePanel slicePanel = new SlicePanel(areaModel.getSlice(sliceDepth));
			slicePanels.add(slicePanel);
			slicePanel.addClickListener(this);
			add(slicePanel, BorderLayout.WEST);
		}

		buttonRight = new JButton();
		buttonRight.setPreferredSize(new Dimension(DEPTH_NAVIGATION_BUTTON_WIDTH, (int)slicePanels.get(0).getPreferredSize().getHeight()));
		buttonLeft.setPreferredSize(new Dimension(DEPTH_NAVIGATION_BUTTON_WIDTH, (int)slicePanels.get(0).getPreferredSize().getHeight()));
		buttonRight.setEnabled(areaModel.getDimensions().getDepth() > visibledSlices);
		buttonRight.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setFirstSiceDepth(firstSiceDepth + 1);
			}
			
		});
		add(buttonRight, BorderLayout.WEST);
		buttonRight.setIcon(new NavigatorIcon("/biz/shipilov/games/miner/view/resources/navigator.right.png"));

		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				setFirstSiceDepth(firstSiceDepth + e.getWheelRotation());
			}
		});
		
		setPreferredSize(getLayout().preferredLayoutSize(this));
		setFirstSiceDepth(firstSiceDepth);
	}
	
	class NavigatorIcon implements Icon {
		private BufferedImage image;
		
		public NavigatorIcon(String imageResource) {
			try {
				image = ImageIO.read(AreaNavigator.class.getResourceAsStream(imageResource));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public int getIconHeight() {
			return 16;
		}

		public int getIconWidth() {
			return 12;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.drawImage(image, x, y, null);
		}
		
	}

	//methods of BaseAreaModelListener
	public void onBlowUp() {
		// TODO 
	}

	public void onChangeCellState(ChangeCellStateEvent event) {
		repaint();
	}

	public void onWinning() {
		// TODO
	}

	private void setFirstSiceDepth(int firstSiceDepth) {
		if (this.firstSiceDepth == firstSiceDepth || firstSiceDepth < 0 || firstSiceDepth > (areaModel.getDimensions().getDepth() - 3))
			return;
		
		this.firstSiceDepth = firstSiceDepth;
		
		for(int i = 0; i < visibledSlices; i++)
			slicePanels.get(i).setAreaSlice(areaModel.getSlice(firstSiceDepth + i));
		
		buttonLeft.setEnabled(firstSiceDepth != 0);
		buttonRight.setEnabled(firstSiceDepth + visibledSlices != areaModel.getDimensions().getDepth());
		
		repaint();
		
	}
	
	public int getFirstSiceDepth() {
		return firstSiceDepth;
	}

	
	
	public class AreaClickEvent {
		private final CellPosition cellPosition;
		private final MouseButton mouseButton;
		
		public AreaClickEvent(CellPosition cellPosition, MouseButton mouseButton) {
			this.cellPosition = cellPosition;
			this.mouseButton = mouseButton;
		}

		public CellPosition getCellPosition() {
			return cellPosition;
		}

		public MouseButton getMouseButton() {
			return mouseButton;
		}
		
		@Override
		public String toString() {
			return new ToStringBuilder(this)
				.append("cellPosition", cellPosition)
				.append("mouseButton", mouseButton)
				.toString();
		}
		
	}
	
	public interface AreaListener {
		public void onClick(AreaClickEvent event);
	}
	
	private void fireAreaEvent(AreaClickEvent event) {
		for(AreaListener listener : listeners)
			listener.onClick(event);
	}
	
	public void addAreaListener(AreaListener listener) {
		listeners.add(listener);
	}

	//SliceClickListener implementations
	public void onClick(ClickEvent event) {
		fireAreaEvent(new AreaClickEvent(new CellPosition(event.getColumn(), event.getRow(), firstSiceDepth + slicePanels.indexOf(event.getSource())), event.getMouseButton()));
	}

	public void onMouseOver(SlicePanel source) {
		for (SlicePanel slicePanel : slicePanels)
			slicePanel.setHighlightedArea(null);
			
		source.setHighlightedArea(null);
		
		if (slicePanels.indexOf(source) - 1 >= 0)
			slicePanels.get(slicePanels.indexOf(source) - 1).setHighlightedArea(source.getTargetCell());

		if (slicePanels.indexOf(source) + 1 < slicePanels.size())
			slicePanels.get(slicePanels.indexOf(source) + 1).setHighlightedArea(source.getTargetCell());
		
	}

	public void setGameOverMode(boolean gameOverMode) {
		for(SlicePanel panel : slicePanels)
			panel.setGameOverMode(gameOverMode);
		repaint();
	}
	
}
