package com.ybsx.util;

import io.github.biezhi.ome.OhMyEmail;
import io.github.biezhi.ome.SendMailException;

import static io.github.biezhi.ome.OhMyEmail.SMTP_QQ;
public class SendEmail {
	
	static{
		OhMyEmail.config(SMTP_QQ(false), "740393778@qq.com", "huwweenoskuwbbcj");
	}
	
	public static void sendEmail(String info){
	        try {
				OhMyEmail.subject("接口程序报错")
				        .from("李劲接口程序")
				        .to("975907209@qq.com")
				        .text(info)
				        .send();
			} catch (SendMailException e) {
				e.printStackTrace();
			}
		
	}	
	
	
}
