package cs518.a4.distributedchat.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Helper {
	public static byte[] getRandomData(int size){
		byte[] data = new byte[size];
		Random randomGenerater = new Random();
		randomGenerater.nextBytes(data);
		return data;
	}

	public static String fromBytes(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA1");
		byte[] hash = digest.digest(data);
		BigInteger hashInt = new BigInteger(1, hash);
		return hashInt.toString(16);
	}
}
