package biz.shipilov.games.miner.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import biz.shipilov.games.miner.model.GameSession.GameLevel;

public class Championship implements Serializable {
	private static final String CHAMPIONS_DATA = "champions.data";

	private static final long serialVersionUID = -7226351703848837773L;
	
	private SortedMap<GameLevel, Champion> champions;
	
	public Championship(List<GameLevel> levels) {
		if (levels == null)
			throw new IllegalArgumentException("levels is null");
		
		champions = restoreChampions();
		if (champions != null)
			return;
		champions = new TreeMap<GameLevel, Champion>();
		
		for(GameLevel level : levels)
			champions.put(level, Champion.DEFAULT);
		persisitChampions();
	}
	
	public void reset() {
		for(GameLevel level : champions.keySet())
			champions.put(level, Champion.DEFAULT);
		persisitChampions();
	}

	private void persisitChampions() {
		try {
			FileOutputStream fos = new FileOutputStream(CHAMPIONS_DATA);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(champions);
			oos.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private SortedMap<GameLevel, Champion> restoreChampions() {
		SortedMap<GameLevel, Champion> result = null;
		try {
			FileInputStream fis = new FileInputStream(CHAMPIONS_DATA);
			ObjectInputStream ois = new ObjectInputStream(fis);
			result = (SortedMap<GameLevel, Champion>) ois.readObject();
			ois.close();
		} catch (Exception exc) {}
		return result;
	}
	
	public void registerChampion(GameLevel level, Champion champion) {
		if (!champions.containsKey(level))
			throw new IllegalArgumentException("level not found");
		champions.put(level, champion);
		persisitChampions();
	}
	
	public SortedMap<GameLevel, Champion> getChampions() {
		return champions;
	}
	
	public static class Champion implements Serializable {
		private static final long serialVersionUID = -7770485420614298730L;

		public static final Champion DEFAULT = new Champion("Анонимный", 9999);
		
		private final String name;
		private final Integer result;

		public Champion(String name, Integer result) {
			this.name = name;
			this.result = result;
		}
		
		public Integer getResult() {
			return result;
		}
		
		public String getName() {
			return name;
		}
	}
}
