package routines;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
/*
 * user specification: the function's comment should contain keys as follows: 1. write about the function's comment.but
 * it must be before the "{talendTypes}" key.
 * 
 * 2. {talendTypes} 's value must be talend Type, it is required . its value should be one of: String, char | Character,
 * long | Long, int | Integer, boolean | Boolean, byte | Byte, Date, double | Double, float | Float, Object, short |
 * Short
 * 
 * 3. {Category} define a category for the Function. it is required. its value is user-defined .
 * 
 * 4. {param} 's format is: {param} <type>[(<default value or closed list values>)] <name>[ : <comment>]
 * 
 * <type> 's value should be one of: string, int, list, double, object, boolean, long, char, date. <name>'s value is the
 * Function's parameter name. the {param} is optional. so if you the Function without the parameters. the {param} don't
 * added. you can have many parameters for the Function.
 * 
 * 5. {example} gives a example for the Function. it is optional.
 */
public class Crypt {

	private static final String KEY_WORD = "PkmJygVfrDxsDeeD";
	private static final int AES_128_KEY_LEN = 16; // 128 bit
	public static String Decode(String password)
	{
		String decodeStr = null;
		byte[] resultBytes = null;
		try {
			resultBytes = encodeAnddecode(decode(password), 0,
					KEY_WORD.getBytes(), 0, 1);
		} catch (Exception e) {

		}

		decodeStr = new String(resultBytes);
		return decodeStr;
	}
	public static byte[] decode(String stData) throws BadPaddingException,
			IllegalBlockSizeException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		byte[] l_btData = null;
		byte[] l_btTmp = null;
		String l_stData = null;
		int l_iLen;
		int ii;
		int jj;
		int kk;
		char l_cTmp;

		if (stData == null) {
			return null;
		}

		l_iLen = stData.length();

		if ((l_iLen % 2) != 0) {
			return null;
		}

		l_stData = stData.toUpperCase();

		for (ii = 0; ii < l_iLen; ii++) {
			l_cTmp = l_stData.charAt(ii);

			if (!((('0' <= l_cTmp) && (l_cTmp <= '9')) || (('A' <= l_cTmp) && (l_cTmp <= 'F')))) {
				return null;
			}
		}

		l_iLen /= 2;

		l_btData = new byte[l_iLen];

		l_btTmp = new byte[2];

		for (ii = 0, jj = 0, kk = 0; ii < l_iLen; ii++) {
			l_btTmp[0] = (byte) (l_stData.charAt(jj++));
			l_btTmp[1] = (byte) (l_stData.charAt(jj++));

			for (kk = 0; kk < 2; kk++) {
				if (('A' <= l_btTmp[kk]) && (l_btTmp[kk] <= 'F')) {
					l_btTmp[kk] -= 55;
				} else {
					l_btTmp[kk] -= 48;
				}
			}

			l_btData[ii] = (byte) ((l_btTmp[0] << 4) | l_btTmp[1]);
		}

		// l_btTmp = null;
		l_stData = null;

		return l_btData;
	}

	private static byte[] encodeAnddecode(byte[] btData, int iLen,
			byte[] btKey, int iKeyLen, int iFlag) throws BadPaddingException,
			IllegalBlockSizeException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		int ii;
		int l_iMode;
		byte[] l_btKey = null;
		Cipher l_oCipher = null;

		if ((btData == null) || (btKey == null)) {
			return null;
		}

		if ((iLen <= 0) || (iLen > btData.length)) {
			iLen = btData.length;
		}

		if ((iKeyLen <= 0) || (iKeyLen > btKey.length)) {
			iKeyLen = btKey.length;
		}

		if (iKeyLen > AES_128_KEY_LEN) // 16 Bytes
		{
			iKeyLen = AES_128_KEY_LEN; // 16 Bytes
		}

		l_btKey = new byte[AES_128_KEY_LEN]; // 16 Bytes

		for (ii = 0; ii < AES_128_KEY_LEN; ii++) {
			l_btKey[ii] = (byte) 0x00;
		}

		for (ii = 0; ii < iKeyLen; ii++) {
			l_btKey[ii] = btKey[ii];
		}

		l_oCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		if (iFlag == 0) {
			l_iMode = Cipher.ENCRYPT_MODE;
		} else {
			l_iMode = Cipher.DECRYPT_MODE;
		}

		l_oCipher.init(l_iMode, new SecretKeySpec(l_btKey, 0, AES_128_KEY_LEN,
				"AES"));

		// l_btKey = null;

		return l_oCipher.doFinal(btData, 0, iLen);
	}

}
