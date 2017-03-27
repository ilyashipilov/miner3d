package biz.shipilov.games.miner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import biz.shipilov.games.miner.model.GameSession.GameLevel;

public class WinnerDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private Panel panel = new Panel();
	private final WinnerDialogSaveListener saveListener;
	private JTextField textField;

	public WinnerDialog(Frame owner, final WinnerDialogSaveListener saveListener) {
		super(owner, true);
		this.saveListener = saveListener;
		add(panel);
		setSize(370, 120);
		setResizable(false);
	}
	
	public static interface WinnerDialogSaveListener {
		void onSave(String newChampionName);
	}
	
	public void setLevel(GameLevel gameLevel) {
		this.setTitle("Вы стали чемпионом среди " + gameLevel.getNameForWinnerDialog() + "!");
		textField.setText("");
	}
	
	class Panel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		Panel() {
			setBackground(new Color(255, 255, 255));
			JLabel message = new JLabel("Введите ваше имя:");
			//message.setPreferredSize(new Dimension(200, 24));
			add(message, BorderLayout.CENTER);
			JButton saveButton = new JButton("Сохранить");
			textField = new JTextField();
			textField.setPreferredSize(new Dimension(300, 24));
			add(textField, BorderLayout.CENTER);
			saveButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (saveListener == null)
						return;
					saveListener.onSave(textField.getText());
				}
				
			});
			
			add(saveButton, BorderLayout.CENTER);
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
	
}
