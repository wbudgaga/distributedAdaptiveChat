package cs518.a4.distributedchat.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
	private final String KEY 	= "Verzeihen ist die beste Rache";
	private final String CIPHER = "DES"; //AES cipher in CBC mode with PKCS #5 padding
	
	private Cipher 			cipher;
	private SecretKey 		secretKey;
	
	public Encryption() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException{
		cipher 			= Cipher.getInstance(CIPHER);
		secretKey = calcSecretKey();
	}
	
 	private SecretKey calcSecretKey() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException{        
        DESKeySpec keySpec = new DESKeySpec(KEY.getBytes("UTF8")); 
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(keySpec);
	}
	
	
	public byte[] encrypt(byte[] textBytes) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return cipher.doFinal(textBytes);		
	}
	
	public byte[] decrypt(byte[] decryptedText) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(decryptedText);
	}

/*	public String decrypt(byte[] decryptedText) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(decryptedText);
        return new String(decrypted, "UTF-8");
	}
*/}
