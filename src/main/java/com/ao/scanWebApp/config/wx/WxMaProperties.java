package com.ao.scanWebApp.config.wx;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@ConfigurationProperties(prefix="wxconfig")
public class WxMaProperties {
    /**
     * ����΢��С�����appid
     */
    private String appid;

    /**
     * ����΢��С�����Secret
     */
    private String secret;

    /**
     * ����΢��С�����token
     */
    private String token;

    /**
     * ����΢��С�����EncodingAESKey
     */
    private String aesKey;

    /**
     * ��Ϣ��ʽ��XML����JSON
     */
    private String msgDataFormat;
    
    /**

     * ΢��֧���̻���

     */

    private String mchId;



    /**

     * ΢��֧���̻���Կ

     */

    private String mchKey;



    /**

     * ������ģʽ�µ����̻������˺�ID����ͨģʽ�벻Ҫ���ã����������ļ��н���Ӧ��ɾ��

     */

    private String subAppId;



    /**

     * ������ģʽ�µ����̻��ţ���ͨģʽ�벻Ҫ���ã���������������ļ��н���Ӧ��ɾ��

     */

    private String subMchId;



    /**

     * apiclient_cert.p12�ļ��ľ���·�����������������Ŀ�У�����classpath:��ͷָ��

     */

    private String keyPath;

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return this.aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getMsgDataFormat() {
        return msgDataFormat;
    }

    public void setMsgDataFormat(String msgDataFormat) {
        this.msgDataFormat = msgDataFormat;
    }

    public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getMchKey() {
		return mchKey;
	}

	public void setMchKey(String mchKey) {
		this.mchKey = mchKey;
	}

	public String getSubAppId() {
		return subAppId;
	}

	public void setSubAppId(String subAppId) {
		this.subAppId = subAppId;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}

	public String getKeyPath() {
		return keyPath;
	}

	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}