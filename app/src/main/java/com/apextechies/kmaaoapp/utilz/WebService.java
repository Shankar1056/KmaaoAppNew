package com.apextechies.kmaaoapp.utilz;

/**
 * Created by Shankar on 2/6/2018.
 */

public class WebService {

    public static String BASE_URL = "http://apextechies.com/kmaao/index.php/";
    public static String LOGIN = BASE_URL+"get_userLogin";
    public static String SIGNUP = BASE_URL+"get_userDetails";
    public static String CATEGORY = BASE_URL+"get_category";
    public static String APPRULES = BASE_URL+"get_applicationRules";
    public static String WALLET = BASE_URL+"get_wallet";
    public static String WALLEHTISTORY = BASE_URL+"get_wallethistory";
    public static String DEVICEID = BASE_URL+"get_deviceId";
    public static String REQUESTAMOUNT = BASE_URL+"get_requestAmount";
    public static String SETWALLETAMOUNT = BASE_URL+"set_walletAmount";
    public static String SETWALLETAMOUNTDIALY = BASE_URL+"set_updateWalletEveryday";
}
