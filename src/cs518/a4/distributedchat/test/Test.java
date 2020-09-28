package cs518.a4.distributedchat.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cs518.a4.distributedchat.util.Encryption;
import cs518.a4.distributedchat.wireformates.ClientInfo;
import cs518.a4.distributedchat.wireformates.RegisterRequest;

public class Test {
	public void pt() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException{
		Encryption e = new Encryption();
		ClientInfo info = new ClientInfo();
		info.setClientID("client112");
		info.setHost("beet");
		info.setPortNum(8002);
		
		RegisterRequest rr = new RegisterRequest();
		rr.setClientInfo(info);
		byte[] dataToBeSent = rr.packMessage();
		 System.out.println(dataToBeSent[0]+" "+dataToBeSent[1]+" "+dataToBeSent[2]+" "+dataToBeSent[3]+" #####around#before encrypt ###  "+ dataToBeSent.length);
		byte [] en = e.encrypt(rr.packMessage());
		 System.out.println(en[0]+" "+en[1]+" "+en[2]+" "+en[3]+" #####around#after encrypt ###  "+ en.length);
		
		RegisterRequest rr1 = new RegisterRequest();
		dataToBeSent = e.decrypt(en);
		 System.out.println(dataToBeSent[0]+" "+dataToBeSent[1]+" "+dataToBeSent[2]+" "+dataToBeSent[3]+" #####around#after dencrypt ###  "+ dataToBeSent.length);
		rr1.initiate(dataToBeSent);
		System.out.print(rr1.getClientInfo().getClientID()+"=====>"+rr1.getClientInfo().getHost()+"  "+rr1.getClientInfo().getPortNum());
	}
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
		new Test().pt();
	}
/*		  try{
		  FileHandler hand = new FileHandler("vk.log");
		  Logger log = Logger.getLogger("log_file");
		  log.addHandler(hand);
		  log.info("Doing strictily1 ");
		  }
		  catch(IOException e){}
		  }
*/}
