package site.binghai.framework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.binghai.framework.service.PayBizServiceFactory;
import site.binghai.framework.config.IceConfig;
import site.binghai.framework.utils.MD5;

//@RestController
//@RequestMapping("/")
public class PayListener extends BaseController {
    @Autowired
    private IceConfig iceConfig;
    @Autowired
    private PayBizServiceFactory payBizServiceFactory;


    @RequestMapping("payNotify")
    public Object payNotify(@RequestParam Long totalPay, @RequestParam String orderId, @RequestParam String sign) {
        if (hasEmptyString(totalPay, orderId, sign)) {
            return fail("all parameters is required.");
        }

        if (!MD5.encryption(totalPay + orderId + iceConfig.getWxValidateMD5Key()).equals(sign)) {
            return fail("Illegal signature");
        }

        try {
            payBizServiceFactory.onPayNotify(orderId);
        } catch (Exception e) {
            logger.error("onPayNotify error !totalPay:{},orderId:{},sign:{}", totalPay, orderId, sign, e);
            return fail(e.getMessage());
        }
        return success();
    }


}
