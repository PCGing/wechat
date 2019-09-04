/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * 
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package com.pcg.helloworld.utils;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * SHA1 class
 *
 * 计算公众平台的消息签名接口.
 */
public class SHA1 {

	/**
	 * &#x7528;SHA1&#x7b97;&#x6cd5;&#x751f;&#x6210;&#x5b89;&#x5168;&#x7b7e;&#x540d;
	 * @param token &#x7968;&#x636e;
	 * @param timestamp &#x65f6;&#x95f4;&#x6233;
	 * @param nonce &#x968f;&#x673a;&#x5b57;&#x7b26;&#x4e32;
	 * @param encrypt &#x5bc6;&#x6587;
	 * @return &#x5b89;&#x5168;&#x7b7e;&#x540d;
	 * @throws Exception
	 */
	public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws Exception
			  {
		try {
			String[] array = new String[] { token, timestamp, nonce, encrypt };
			StringBuffer sb = new StringBuffer();
			// 字符串排序
			Arrays.sort(array);
			for (int i = 0; i < 4; i++) {
				sb.append(array[i]);
			}
			String str = sb.toString();
			// SHA1签名生成
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] digest = md.digest();

			StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			//throw new AesException(AesException.ComputeSignatureError);
		}
		return encrypt;
	}
}
