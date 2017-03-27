package biz.shipilov.games.miner.view;

import java.awt.Frame;
import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class AboutDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public AboutDialog(Frame owner) {
		super(owner, true);
		setTitle("О программе");
		add(new Panel());
		setSize(460, 106);
		setResizable(false);
	}

	class Panel extends JPanel {
		private static final long serialVersionUID = 1L;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			try {
				g.drawImage(ImageIO.read(SlicePanel.class.getResourceAsStream("/biz/shipilov/games/miner/view/resources/about.png")), 0, 0, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
