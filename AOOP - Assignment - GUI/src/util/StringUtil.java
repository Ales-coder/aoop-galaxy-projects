package util;

import java.util.Random;

import app.Settings;

public class StringUtil {
	public static String randomUpperCase() {
		int firstUppercaseIndex = (int)'A'; 
	    Random r = Settings.instance().getRandom();
		int letterIndex = r.nextInt(26); 
		char randomUppercase = (char) (firstUppercaseIndex + letterIndex);
		return Character.toString(randomUppercase);
	}
	
	public static int getIndexOfNonWhitespaceAfterWhitespace(String string){
	    char[] characters = string.toCharArray();
	    boolean lastWhitespace = false;
	    for(int i = 0; i < string.length(); i++){
	        if(Character.isWhitespace(characters[i])){
	            lastWhitespace = true;
	        } else if(lastWhitespace){
	            return i;
	        }
	    }
	    return -1;
	}
}
