package biz.shipilov.games.miner.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import biz.shipilov.games.miner.model.GameSession;
import biz.shipilov.games.miner.model.GameSessionListener;
import biz.shipilov.games.miner.model.GameSession.GameLevel;
import biz.shipilov.games.miner.view.WinnerDialog.WinnerDialogSaveListener;

public class GameFrame extends JFrame implements GameSessionListener {

	private static final long serialVersionUID = 1L;
	private final GameSession gameSession;
	private GameView gameView;
	private ChampionshipDialog championshipDialog;
	private WinnerDialog winnerDialog;
	private AboutDialog aboutDialog;
	private HelpDialog helpDialog;
	
	public GameFrame(GameSession session) {
		this.gameSession = session;
		this.championshipDialog = new ChampionshipDialog(this, session.getChampionship());
		this.winnerDialog = new WinnerDialog(this, new WinnerDialogSaveListener() {

			public void onSave(String newChampionName) {
				gameSession.inputChampionName(newChampionName);
				winnerDialog.setVisible(false);
				championshipDialog.refresh();
				championshipDialog.setLocationRelativeTo(GameFrame.this);
				championshipDialog.setVisible(true);
			}
			
		});
		this.aboutDialog = new AboutDialog(this);
		this.helpDialog = new HelpDialog(this);
		this.gameSession.addListener(this);

		JMenuBar menu = new JMenuBar();
		JMenu gameMenu = new JMenu("Игра");
		JMenuItem newGame = new JMenuItem("Новая игра");
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameFrame.this.gameSession.startNewGame();
			}
		});
		
		gameMenu.add(newGame);
		gameMenu.addSeparator();
		
		for (GameLevel level : GameSession.LEVELS) {
			NewLevelMenuItem item = new NewLevelMenuItem(level);
			if (level.equals(GameSession.BEGINNER_LEVEL)) {
				currentLevelItem = item;
				item.setSelected(true);
			}
			gameMenu.add(item);
		}
		
		gameMenu.addSeparator();
		JMenuItem championsItem = new JMenuItem("Чемпионы");
		championsItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				championshipDialog.setLocationRelativeTo(GameFrame.this);
				championshipDialog.setVisible(true);
			}
			
		});
		gameMenu.add(championsItem);
		gameMenu.addSeparator();
		JMenuItem exitItem = new JMenuItem("Выход");
		exitItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
			
		});
		
		gameMenu.add(exitItem);
		menu.add(gameMenu);
		
		JMenu helpMenu = new JMenu("Справка");
		JMenuItem helpItem = new JMenuItem("Помощь");
		helpItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				helpDialog.setLocationRelativeTo(GameFrame.this);
				helpDialog.setVisible(true);
			}
		});

		helpMenu.add(helpItem);
		helpMenu.addSeparator();
		JMenuItem aboutItem = new JMenuItem("О программе...");
		aboutItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				aboutDialog.setLocationRelativeTo(GameFrame.this);
				aboutDialog.setVisible(true);
			}
		});
		helpMenu.add(aboutItem);
		menu.add(helpMenu);
		
		setTitle("Сапер 3D");
		setJMenuBar(menu);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

	}

	//GameSessionListener impls
	public void onNewGame(GameSessionEvent event) {
		if (gameView != null)
			remove(gameView);
		gameView = new GameView(event.getGame());
		add(gameView);
		if (getSize().equals(gameView.getPreferredSize())) {
			gameView.revalidate();
			return;
		}
		setVisible(false);
		setSize(new Dimension( (int)gameView.getPreferredSize().getWidth(), (int)gameView.getPreferredSize().getHeight()));
		setLocationRelativeTo( null );
		setVisible(true);

	}

	public void onNewChampion(GameSessionEvent event) {
		gameView.repaint();
		winnerDialog.setLocationRelativeTo(this);
		winnerDialog.setLevel(event.getSession().getCurrentGameLevel());
		winnerDialog.setVisible(true);
		
	}
	
	class RepeatLevelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameSession.startNewGame();
		}
	}
	
	private NewLevelMenuItem currentLevelItem;
	
	class NewLevelMenuItem extends JRadioButtonMenuItem {
		private static final long serialVersionUID = 1L;
		
		private final GameLevel level;
		
		public NewLevelMenuItem(GameLevel level) {
			super(level.getName());
			this.level = level;
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currentLevelItem.setSelected(false);
					gameSession.startNewGame(NewLevelMenuItem.this.level);
					currentLevelItem = NewLevelMenuItem.this; 
				}
			});
		}
	}

}
