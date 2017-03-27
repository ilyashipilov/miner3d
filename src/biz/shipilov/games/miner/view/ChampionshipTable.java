package biz.shipilov.games.miner.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

import biz.shipilov.games.miner.model.Championship;
import biz.shipilov.games.miner.model.GameSession.GameLevel;

public class ChampionshipTable extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String LEVEL_SUFFIX = ":";

	public ChampionshipTable(Championship championship) {
		setBackground(new Color(255, 255, 255));
		setLayout(new GridLayout(championship.getChampions().size(), 3));
		for(GameLevel level : championship.getChampions().keySet()) {
			add(new Label(level.getName() + LEVEL_SUFFIX));
			add(new Label(MessageUtil.prepareRecordMessage(championship.getChampions().get(level).getResult())));
			add(new Label(championship.getChampions().get(level).getName()));
		}
		setPreferredSize(new Dimension(350, championship.getChampions().size() * 30));
	}

	class Label extends JLabel {
		private static final long serialVersionUID = 1L;
		public Label(String text) {
			super(text);
			setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		super.paintComponent(g);
	}
}
