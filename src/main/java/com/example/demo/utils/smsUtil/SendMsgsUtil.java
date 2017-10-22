package com.example.demo.utils.smsUtil;

import javax.xml.transform.Templates;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 发送短信Util
 */
public class SendMsgsUtil {

    /**
     * 发送短信
     *
     * @param mobileNo
     * @param content
     * @return
     */
    public static String sendSmsDirectly(String mobileNo, String content) {
        if (!validMobileNo(mobileNo)) {
            return SmsEnum.PHONE_ERROR.getValue();
        }

        // 短信发送参数
        Map<String, String> smsParams = new HashMap<String, String>();
        // 通道用户名
        smsParams.put("u", SmsEnum.SMS_CHANNEL_U_NAME.getValue());
        // 通道密码
        smsParams.put("p", SmsEnum.SMS_CHANNEL_PWD.getValue());
        // 接收短信的手机号码
        smsParams.put("m", mobileNo);
        // 短信内容
        smsParams.put("c", content);

        //发送HTTP请求
        HttpConnectionUtil conn = new HttpConnectionUtil(SmsEnum.SMS_CHANNEL_URL.getValue());

        try {
            conn.init();
            byte[] bys = conn.postParams(smsParams, true);
            String result = new String(bys, "UTF-8");
            if (result != null && "0".equals(result)) {
                return "Send success.";
            } else {
                return "Send fail.";
            }
        } catch (Exception e) {
            return "发送短信失败，mobileNo/content >> " + mobileNo + "/" + content +
                    "，error msgs > " + e.getMessage();
        }
    }

    /**
     * 获取随机数
     *
     * @param j:几位随机数
     * @return
     */
    public static String getAuthCode(int j) {
        Random random = new Random();
        StringBuffer code = new StringBuffer();
        for (int i = 1; i <= j; i++) {
            code.append(random.nextInt(10) % (11) + 0);
        }
        return code.toString();
    }

    //-----------私有方法-----------

    /**
     * 验证手机号
     *
     * @param mobileNo
     * @return
     */
    private static boolean validMobileNo(String mobileNo) {
        if (mobileNo != null) {
            try {
                long mobile = Long.parseLong(mobileNo.trim());
                if ((mobile >= 13000000000L && mobile <= 13999999999L)
                        || (mobile >= 15000000000L && mobile <= 15999999999L)
                        || (mobile >= 17000000000L && mobile <= 17999999999L)
                        || (mobile >= 18000000000L && mobile <= 18999999999L)) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println("手机号码格式有误！");
            }
        }
        return false;
    }
}
