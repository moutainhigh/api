package com.zhsj.api.bean;

/**
 * Created by lcg on 17/1/8.
 */
public class MSStoreBean {
    private String storeNo;
    private String reg_contact_tel; //商户手机号
    private String legal_person;//法定代表人姓名
    private String legal_person_id; //法定代表人身份证号
    private String mer_email ; //商户联系邮箱
    private String filed1; //入驻商户的客服电话
    private String agent_no; //"95272016121410000062");//此字符串由民生提供
    private String wx_business_type;//商户营业类别
    private String ali_business_type; //支付宝口碑类目
    private String mer_name ; //商户全称
    private String wx_rate;  //微信费率     示例:0.5，代表和该入驻商户签约千分之5的费率
    private String ali_rate;   //支付宝费率     示例:0.5，代表和该入驻商户签约千分之5的费率
    private String sa_name;   //结算账户名称       G
    private String sa_num;   //结算账户账号
    private String sa_bank_name;  //结算账户银行
    private String sa_bank_type;   //结算账户类型(对公=01)(对私=00)
    private String settlement_type;   //商户结算类型D0:实时清算,T1:隔天清算 若不传，默认D0清算
    private String user_pid; //支付宝ISV的pid
    private String mer_short_name;//商户简称
    private int step;

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getReg_contact_tel() {
        return reg_contact_tel;
    }

    public void setReg_contact_tel(String reg_contact_tel) {
        this.reg_contact_tel = reg_contact_tel;
    }

    public String getLegal_person() {
        return legal_person;
    }

    public void setLegal_person(String legal_person) {
        this.legal_person = legal_person;
    }

    public String getLegal_person_id() {
        return legal_person_id;
    }

    public void setLegal_person_id(String legal_person_id) {
        this.legal_person_id = legal_person_id;
    }

    public String getMer_email() {
        return mer_email;
    }

    public void setMer_email(String mer_email) {
        this.mer_email = mer_email;
    }

    public String getFiled1() {
        return filed1;
    }

    public void setFiled1(String filed1) {
        this.filed1 = filed1;
    }

    public String getAgent_no() {
        return agent_no;
    }

    public void setAgent_no(String agent_no) {
        this.agent_no = agent_no;
    }

    public String getWx_business_type() {
        return wx_business_type;
    }

    public void setWx_business_type(String wx_business_type) {
        this.wx_business_type = wx_business_type;
    }

    public String getAli_business_type() {
        return ali_business_type;
    }

    public void setAli_business_type(String ali_business_type) {
        this.ali_business_type = ali_business_type;
    }

    public String getMer_name() {
        return mer_name;
    }

    public void setMer_name(String mer_name) {
        this.mer_name = mer_name;
    }

    public String getWx_rate() {
        return wx_rate;
    }

    public void setWx_rate(String wx_rate) {
        this.wx_rate = wx_rate;
    }

    public String getAli_rate() {
        return ali_rate;
    }

    public void setAli_rate(String ali_rate) {
        this.ali_rate = ali_rate;
    }

    public String getSa_name() {
        return sa_name;
    }

    public void setSa_name(String sa_name) {
        this.sa_name = sa_name;
    }

    public String getSa_num() {
        return sa_num;
    }

    public void setSa_num(String sa_num) {
        this.sa_num = sa_num;
    }

    public String getSa_bank_name() {
        return sa_bank_name;
    }

    public void setSa_bank_name(String sa_bank_name) {
        this.sa_bank_name = sa_bank_name;
    }

    public String getSa_bank_type() {
        return sa_bank_type;
    }

    public void setSa_bank_type(String sa_bank_type) {
        this.sa_bank_type = sa_bank_type;
    }

    public String getSettlement_type() {
        return settlement_type;
    }

    public void setSettlement_type(String settlement_type) {
        this.settlement_type = settlement_type;
    }

    public String getUser_pid() {
        return user_pid;
    }

    public void setUser_pid(String user_pid) {
        this.user_pid = user_pid;
    }

    public String getMer_short_name() {
        return mer_short_name;
    }

    public void setMer_short_name(String mer_short_name) {
        this.mer_short_name = mer_short_name;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
