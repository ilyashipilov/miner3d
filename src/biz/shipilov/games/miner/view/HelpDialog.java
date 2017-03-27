package biz.shipilov.games.miner.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

public class HelpDialog extends JDialog {
	
	public HelpDialog(Frame owner) {
		super(owner, true);
		setBackground(new Color(255, 255, 255 ));
		add(new Panel(), BorderLayout.CENTER);
		setLayout(new GridLayout(1,1));
	    setSize(300, 400);
	    setTitle("Помощь");
	    setResizable(false);
	}
	
	class Panel extends JPanel {
		Panel() {
		    JEditorPane comp = new JEditorPane("text/html", getText());
		    comp.setEditable(false);
		    comp.setPreferredSize(new Dimension(295, 410));
		    setBackground(new Color(255, 255, 255 ));
			add(comp);
		}
		
		String getText() {
		    String content = null;
		    try {
		      BufferedReader in = new BufferedReader(new InputStreamReader(HelpDialog.class.getResourceAsStream("/biz/shipilov/games/miner/view/resources/help.html")));
		      String str = null;
		      content = "";
		      do {
		        str = in.readLine();
		        if (str == null) break;
		        content = content + str;
		      } while (str != null);
		    }
		    catch(Exception e)
		    {
		      e.printStackTrace();
		    }
		    return content;
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
