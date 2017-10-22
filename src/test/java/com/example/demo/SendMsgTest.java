package com.example.demo;

import com.example.demo.utils.smsUtil.SendMsgsUtil;
import com.example.demo.utils.smsUtil.SmsEnum;

/**
 * @author yongsheng.he
 * @describe 发送短信测试
 * @date 2017/10/19 14:00
 */
public class SendMsgTest {
    public static void main(String[] args){
        SendMsgs();
    }
    public static void SendMsgs(){
        //短信模板
        String msgsTemplate = SmsEnum.CONTENT_TEMPLATE_REGISTER.getValue();
        //4位随机数
        String str= SendMsgsUtil.getAuthCode(4);
        System.out.println("验证码："+str);
        //替换模板里的内容
        String msgsContent = msgsTemplate.replace("AUTH_CODE", str);
        //发送短信
       String s=SendMsgsUtil.sendSmsDirectly("18210372703", msgsContent);
        System.out.println(s);
    }
}
