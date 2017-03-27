package biz.shipilov.games.miner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import biz.shipilov.games.miner.model.Championship;

public class ChampionshipDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final Championship championship;
	private Panel panel;
	
	public ChampionshipDialog(Frame owner, Championship championship) {
		super(owner, true);
		this.championship = championship;
		panel = new Panel();
		setResizable(false);
		add(panel, BorderLayout.CENTER);
		setSize(panel.getPreferredSize());
	}

	public void refresh() {
		panel.refresh();
	}
	
	class Panel extends JPanel {
		private static final long serialVersionUID = 1L;
		private ChampionshipTable table;
		private JButton resetButton;
		private JButton closeButton;
		
		public Panel() {
			refresh();
			setPreferredSize(new Dimension((int)table.getPreferredSize().getWidth() + 40, (int)table.getPreferredSize().getHeight() + 50 + 20));
		}

		public void refresh() {
			setBackground(new Color(255, 255, 255));
			if (table != null) 
				remove(table);
			if (resetButton != null) 
				remove(resetButton);
			if (closeButton != null) 
				remove(closeButton);
			
			table = new ChampionshipTable(championship);
			setTitle("Чемпионы по категориям");
			add(table);
			closeButton = new JButton("Закрыть");
			closeButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ChampionshipDialog.this.setVisible(false);
				}
				
			});
			add(closeButton);

			resetButton = new JButton("Сбросить результаты");
			resetButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					championship.reset();
					remove(table);
					remove(resetButton);
					remove(closeButton);
					table = new ChampionshipTable(championship);
					add(table);
					add(closeButton);
					add(resetButton);
					table.revalidate();
				}
				
			});
			add(resetButton, BorderLayout.SOUTH);
		}
		
		
	}
	
}
