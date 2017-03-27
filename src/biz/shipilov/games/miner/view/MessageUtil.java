package biz.shipilov.games.miner.view;

import java.text.MessageFormat;

public class MessageUtil {
	private final static MessageFormat secondsMessageForm1 = new MessageFormat("прошли {0} секунд");
	private final static MessageFormat secondsMessageForm2 = new MessageFormat("прошла {0} секунда");
	private final static MessageFormat secondsMessageForm3 = new MessageFormat("прошли {0} секунды");

	private final static MessageFormat recordMessageForm1 = new MessageFormat("{0} секунд");
	private final static MessageFormat recordMessageForm2 = new MessageFormat("{0} секунда");
	private final static MessageFormat recordMessageForm3 = new MessageFormat("{0} секунды");
	
	private final static MessageFormat minesMessageForm1 = new MessageFormat("остались {0} мин");
	private final static MessageFormat minesMessageForm2 = new MessageFormat("осталасть {0} мина");
	private final static MessageFormat minesMessageForm3 = new MessageFormat("остались {0} мины");
	
	private final static MessageFormat summaryMessageForm1 = new MessageFormat("поле успешно разминировано за {0} секунд!");
	private final static MessageFormat summaryMessageForm2 = new MessageFormat("поле успешно разминировано за {0} секунду!");
	private final static MessageFormat summaryMessageForm3 = new MessageFormat("поле успешно разминировано за {0} секунды!");
	
	private final static MessageFormat summaryMessage = new MessageFormat("{0} и {1}");
	
	public static String prepareSecondsMessage(Integer seconds) {
		return prepareMessage(seconds, new MessageFormat[] {secondsMessageForm1, secondsMessageForm2, secondsMessageForm3});
	}

	public static String prepareSummaryMessage(Integer seconds) {
		return prepareMessage(seconds, new MessageFormat[] {summaryMessageForm1, summaryMessageForm2, summaryMessageForm3});
	}
	
	public static String prepareMineMessage(Integer count) {
		return prepareMessage(count, new MessageFormat[] {minesMessageForm1, minesMessageForm2, minesMessageForm3});
	}
	
	public static String prepareInfoMessage(Integer seconds, Integer minesCount) {
		return summaryMessage.format(new String[] {prepareSecondsMessage(seconds), prepareMineMessage(minesCount)});
	}

	public static String prepareRecordMessage(Integer seconds) {
		return prepareMessage(seconds, new MessageFormat[] {recordMessageForm1, recordMessageForm2, recordMessageForm3});
	}
	
	private static String prepareMessage(Integer number, MessageFormat[] formats) {
		if (number % 100 >= 10 && number % 100 <= 20 
				|| number % 10 >= 5 && number % 10 <= 9 || number % 10 == 0)
			return formats[0].format(new Integer[] {number});
		if (number % 10 == 1)
			return formats[1].format(new Integer[] {number});
		return formats[2].format(new Integer[] {number});
	}

}
