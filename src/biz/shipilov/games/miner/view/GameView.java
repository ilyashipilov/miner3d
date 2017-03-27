package biz.shipilov.games.miner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

import biz.shipilov.games.miner.model.Game;
import biz.shipilov.games.miner.model.GameListener;
import biz.shipilov.games.miner.view.AreaNavigator.AreaClickEvent;
import biz.shipilov.games.miner.view.AreaNavigator.AreaListener;
import biz.shipilov.games.miner.view.SlicePanel.MouseButton;

public class GameView extends JPanel implements GameListener {
	private static final long serialVersionUID = 1L;

	private static final int INFO_LABEL_HEIGHT = 40;
	private static final int PADDING = 30;
	
	private static final int VISIBLED_SLICES = 3;
	private final Game game;
	private final AreaNavigator areaNavigator;
	private final JLabel infoLabel;
	private int seconds = 0;
	private int markedCellsCount = 0;

	public GameView(Game gameModel) {
		this.game = gameModel;
		gameModel.addGameListener(this);
		areaNavigator = new AreaNavigator(gameModel.getAreaModel(), VISIBLED_SLICES);
		
		setBackground(new Color(255, 255, 255 ));
		
		gameModel.getAreaModel().addAreaModelListener(areaNavigator);
		
		areaNavigator.addAreaListener(new AreaListener() {

			public void onClick(AreaClickEvent event) {
				if (event.getMouseButton() == MouseButton.LEFT)
					game.openCell(event.getCellPosition());
				else if (event.getMouseButton() == MouseButton.RIGHT)
					game.markCell(event.getCellPosition());
				else {
					game.openRegion(event.getCellPosition());
				}
			}
			
		});
		
		infoLabel = new JLabel("игра начнется с первого шага");
		infoLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		
		infoLabel.setPreferredSize(new Dimension((int)areaNavigator.getPreferredSize().getWidth(), INFO_LABEL_HEIGHT));
		add(infoLabel, BorderLayout.NORTH);
		add(areaNavigator, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension((int)areaNavigator.getPreferredSize().getWidth() + PADDING * 2, (int)areaNavigator.getPreferredSize().getHeight() + INFO_LABEL_HEIGHT  + PADDING * 3));
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		super.paintComponent(g);
	}
	
	//GameListener implements
	public void onLoss() {
		infoLabel.setText("попробуйте еще разок…");
		areaNavigator.setGameOverMode(true);
	}

	public void onWinning(int seconds) {
		infoLabel.setText(MessageUtil.prepareSummaryMessage(seconds));
	}

	public void onTick(int seconds) {
		this.seconds = seconds;
		updateStateMessage();
	}

	public void onMarkedCellsCountChange(int markedCellsCount) {
		this.markedCellsCount = markedCellsCount;
		updateStateMessage();
	}
	
	private void updateStateMessage() {
		infoLabel.setText(MessageUtil.prepareInfoMessage(seconds, game.getAreaModel().getDimensions().getMinesCount() - markedCellsCount));
	}

}
