package me.zhaotb.test;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;

public class DESUtil {

    public static final String CIPHER_ALGORITHM = "DES/ECB/NoPadding";
    public static final String KEY_ALGORITHM = "DES";
    private static byte[] DES_KEY = { 0x12, (byte) 0x8A, 0x32, (byte) 0x98, 0x7B, 0x52, (byte) 0x91, 0x47, (byte) 0x9C,
            0x0F, 0x2D, 0x60, (byte) 0xD9, (byte) 0xEE, (byte) 0xA1, 0x3B };

    private static byte[] CBS_DB_DATA_KEY = { 0x12, (byte) 0x8A, 0x32, (byte) 0x98, 0x7B, 0x52, (byte) 0x91, 0x47 };
    private static byte[] CBSDYNSQL_KEY = { (byte) 0x9C, 0x0F, 0x2D, 0x60, (byte) 0xD9, (byte) 0xEE, (byte) 0xA1,
            0x3B };
    private static IvParameterSpec iv = new IvParameterSpec(
            new byte[] { (byte) 0xED, 0x29, (byte) 0xA5, 0x37, 0x7B, 0x1C, 0x58, 0x64 });

    public static byte[] doCryptBasedDes(byte[] datas, int mode, int keyType) {
        try {
            //DESKeySpec deskey = new DESKeySpec(DES_KEY);
            //DESKeySpec deskey = new DESKeySpec(DES_KEY);
            DESKeySpec deskey = null;
            if (keyType == 1) {
                deskey = new DESKeySpec(CBS_DB_DATA_KEY); //加解密cbs数据
            } else {
                deskey = new DESKeySpec(CBSDYNSQL_KEY);//加解密cbsdynsql服务参数
            }
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            SecretKey key = keyFactory.generateSecret(deskey);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            int mode1 = mode;

            cipher.init(mode1, key);
            // cipher2.init(mode2, key2, iv);

            //byte [] datas=data.getBytes("utf-8");

            int mod = datas.length % 8;
            byte[] real = datas;
            if (mod != 0) {
                real = new byte[datas.length - mod + 8];
                for (int i = 0; i < datas.length; i++) {
                    real[i] = datas[i];
                }
            }
            //System.out.println(bytesToHexString(real));
            byte[] data1 = cipher.doFinal(real);
            //byte[] data2 = cipher2.doFinal(data1);
            //byte[] data3 = cipher.doFinal(data2);

            return data1;
        } catch (Exception e) {
            //            log.error("加密错误，错误信息：", e);
            e.printStackTrace();
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        // return encryptedData;
    }

    public static String encrypt(String data, int keyType) {
        try {
            byte[] rest = doCryptBasedDes(data.getBytes("GBK"), Cipher.ENCRYPT_MODE, keyType);
            return bytesToHexString(rest);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        throw new RuntimeException("---");
    }

    public static String decrypt(String data, int keyType) {
        try {
            byte[] datas1 = hexStringToBytes(data);
            byte[] datas = doCryptBasedDes(datas1, Cipher.DECRYPT_MODE, keyType);
            int len = 0;
            for (int i = datas.length - 1; i >= 0; i--) {
                if (datas[i] == 0) {
                    len++;
                } else {
                    break;
                }
            }

            //System.out.println(bytesToHexString(datas));
            byte[] real = new byte[datas.length - len];
            for (int i = 0; i < real.length; i++) {
                real[i] = datas[i];
            }
            return new String(real, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解密失败，" + e.getMessage());
        }

    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * 数组转成十六进制字符
     *
     * @param
     * @return HexString
     */
    public static String toHexString1(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i) {
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString();
    }

    public static String toHexString1(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString
     *            the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String encryptCBSData(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return encrypt(str, 1);
    }

    public static String decryptCBSData(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return decrypt(str, 1).replaceAll("\b", ""); //剔除\b是为了兼容历史数据，历史加密数据有多出\b的情况
    }

    public static String encryptCBSDYNSQL(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return encrypt(str, 2);
    }

    public static String decryptCBSDYNSQL(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return decrypt(str, 2);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //		// TODO Auto-generated method stub
        //		String str1 ="insert into ratable_resource_log (OPER_DATE, BILLING_DATE, STAFF_ID, POST_ID, OLD_MSINFO_ID,NEW_MSINFO_ID, OLD_PRICING_ID,NEW_PRICING_ID, OLD_RATABLE_ID, NEW_RATABLE_ID, BALANCE, ADJUST_BALANCE, ADJUST_REASON,OPER_TYPE ) values (sysdate, to_date('20161001', 'yyyy-mm-dd'), '1213100376', '2005061753', '0', '5629432720', '0', '1148819', '0', '401140000', '0', 'null', '中午测试中文测试',1)";
        //		//String newStr1 = new String(str1.getBytes("utf-8"),"GBK");
        //		//String str= new String(str1.getBytes("gbk"));
        //		//System.out.println( str);
        //		//System.out.println( encryptCBSDYNSQL(str));
        //		String result = encryptCBSDYNSQL(str1);
        //		System.out.println( result);
        //		System.out.println( decryptCBSDYNSQL(result));
        System.out.println(decryptCBSData(
                "23C342E627E884B15BDEA7B65A24EA68F6A400B4D065BCFB966B63D1489E86D4D6701F7FB8DC57FE4135DF551622E09C38502DF497530F1A4B8F5C895A978B166AE6E622C89B020DD242D56881B50178C2EF8B18067AE466CDF6E307BC332656E5B17C216A383E04BE9B7972D0FCE961E10B9EAA5D748A8B8868046DFC209ACD"));
        //		/*//EC394ADB8480844876D88D6A46E7D5CC E1A436A57011ADE9121DCF89CB3B29DD    f7fda46a87d54dbd
        //		String result = encryptCBSDYNSQL(str1);
        //		System.out.println( result);
        //		String r2="2D14AE39AC2F5AFC8C9C51B5F1DE772F86981A177D7ADDFE5464660ABFE5C5FEAE2E861DB355418D5D3F6A9F9FC09747DF8FEF8FF29307509B6578193D3BCB65B01E0ADC10936210";
        //		String dr2 = decryptCBSDYNSQL(r2);
        //		String drr2 = new String(str1.getBytes("GBK"),"utf-8");
        //		System.out.println( decryptCBSDYNSQL(r2));
        //		System.out.println( drr2);*/
        //		//System.out.println( encryptCBSDYNSQL(newStr1));
        String dStr = decryptCBSDYNSQL(
                "23C342E627E884B15BDEA7B65A24EA68F6A400B4D065BCFB966B63D1489E86D4D6701F7FB8DC57FE4135DF551622E09C38502DF497530F1A4B8F5C895A978B166AE6E622C89B020DD242D56881B50178C2EF8B18067AE466CDF6E307BC332656E5B17C216A383E04BE9B7972D0FCE961E10B9EAA5D748A8B8868046DFC209ACD");
        System.out.println(new String(dStr.getBytes("GBK"), "utf-8"));
        //
        //
        System.out.println(DESUtil.decryptCBSData("0903339625635654"));

        System.out.println(DESUtil.encryptCBSData("123456Ab"));
        System.out.println(DESUtil.decrypt("7AC52E87717A957075714153C019A6F4", 1));

        System.out.println(DESUtil.encryptCBSDYNSQL("update ts_acct set bill_table_id=1  where TS_BAT_ID=15188"));
        System.out.println(DESUtil.encryptCBSDYNSQL("update batch_adjust set city_id=757 where deal_org_id =0"));
        //        System.out.println(DESUtil.decryptCBSData("BD13A3A8F23EB5E11525D6DA05E16E3E"));
        //
        //        System.out.println(DESUtil.decryptCBSData("fee453f62e00db47"));

        //		System.out.println(decryptCBSData("69F1F9F4DD2B0BBD121DCF89CB3B29DD"));
        //

        //        System.out.println(decryptCBSData("7f0b40ad8eedb88c"));
        //        System.out.println("123456Ab加密后：" + encryptCBSData("123456Ab"));
        //        System.out.println(decryptCBSData("0903339625635654"));
        //        System.out.println(decryptCBSDYNSQL("0798C6F016BB3F45"));
    }

}
