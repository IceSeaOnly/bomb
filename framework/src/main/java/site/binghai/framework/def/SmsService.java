package site.binghai.framework.def;

public interface SmsService {
    String sendVerifyCodeSms(String to, String code);
}
