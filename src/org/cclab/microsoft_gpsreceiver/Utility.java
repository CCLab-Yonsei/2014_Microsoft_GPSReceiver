package org.cclab.microsoft_gpsreceiver;

public class Utility {
	public static String getTwoDigitNumber(final int number) {
		return (number < 10 && number > 0) ? "0" + Integer.toString(number) : Integer.toString(number);
	}
}
