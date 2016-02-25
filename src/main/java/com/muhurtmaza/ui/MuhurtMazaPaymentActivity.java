package com.muhurtmaza.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.muhurtmaza.R;
import com.muhurtmaza.utility.AppConstants;
import com.muhurtmaza.utility.AppPreferences;
import com.muhurtmaza.utility.ConnectionDetector;
import com.muhurtmaza.widget.MMToast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MuhurtMazaPaymentActivity extends ParentActivity {

    private String TAG = getClass().getSimpleName();
    private WebView webView;
    private ProgressDialog mProgressDialog;
    ConnectionDetector conDetect;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muhurt_maza_payment);

        mContext = MuhurtMazaPaymentActivity.this;
        conDetect = new ConnectionDetector();

        webView = (WebView) findViewById(R.id.webView_MuhurtMazaPayment);
        WebSettings settings = webView.getSettings();
        //settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


        Intent intent = getIntent();
        if (intent != null) {

            String bookingId = intent.getStringExtra(AppConstants.POOJA_BOOKING_ID);
            String bookingAmt = intent.getStringExtra(AppConstants.COST_OF_POOJA);
            String phoneNumber = intent.getStringExtra(AppConstants.CONTACT_NO_FOR_POOJA);

            if (conDetect.checkConnectivity(mContext)) {

                new CallAtomGateWay().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bookingId, bookingAmt, phoneNumber);

            } else {
                MMToast.getInstance().showLongToast(AppConstants.API_STATUS_FALSE_MESSAGE,mContext);
            }
        }

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               showLoadingDialog();
                //	Log.d("URL=====", url);
                view.loadUrl(url);
                return true;

            }

            public void onPageFinished(WebView view, String url) {
              dismissLoadingDialog();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //     Log.e("", "Error: " + description);
//	                handler.proceed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(mContext, MainDrawerActivity.class);
        startActivity(intent);
        finish();
    }

    private class CallAtomGateWay extends AsyncTask<String, String, String> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showLoadingDialog();
        }

        @Override
        protected String doInBackground(String... string) {

            String response = atomFunction(string[0], string[1], string[2]);


            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

//			TLS Protocal
//			TLS 1.0  & 1.1, 1.2
          dismissLoadingDialog();
            if (!TextUtils.isEmpty(result)) {
                //	Log.d("WebView", "Result = "+result);
                webView.loadUrl(result);

            } else {
                MMToast.getInstance().showLongToast(AppConstants.API_STATUS_FALSE_MESSAGE, mContext);
            }
        }
    }

    /**
     * This function is used to generate URL for the first call to ATOM Gateway
     * and get the generated URL which is used to redirect user to the respective
     * merchant's site. From that page rest of the flow of the pages will be
     * managed by respective merchant.
     *
     * @param bookingId
     * @param bookingAmount
     * @param phoneNumber
     * @return
     */
    private String atomFunction(String bookingId, String bookingAmount, String phoneNumber) {

        String Atom2Request = "";

        try {

            String xmlURL = "";
            String xmlttype = "";
            String xmltempTxnId = "";
            String xmltoken = "";
            String xmltxnStage = "";

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
            String CurrDateTime = sdf.format(new Date()).toString();


            AppPreferences appPreferences = AppPreferences.getInstance(mContext);
            String email = appPreferences.getString(AppConstants.USER_EMAIL, "");
            //String number = muhurtMazaPreferences.getString(Constants.USER_NUMBER);
            String name = appPreferences.getString(AppConstants.USER_NAME, "");


            Random rnd = new Random();
            int txnId = 100000 + rnd.nextInt(900000);

            String loginId = "160";
            String password = "Test@123";
            String productId = "NSE";

            String clientCode = "107";
            byte[] out = clientCode.getBytes("UTF-8");
            String b64ClientCode = Base64.encodeToString(out, Base64.NO_WRAP); //bookingAmount

            bookingAmount="1000";

            String vVenderURL = "https://paynetzuat.atomtech.in/paynetz/epi/fts?login=" + loginId + "&pass=" + password +
                    "&ttype=NBFundTransfer&prodid=" + productId + "&amt=" + bookingAmount + "&txncurr=INR&txnscamt=0&clientcode="
                    + b64ClientCode + "&txnid=" + txnId + "&date=" + CurrDateTime +
                    "&custacc=888888888888&ru=http://192.168.0.127:8045/mobile/payment_gateway_response/" + "&udf1=" + name + "&udf2="
                    + email + "&udf3=" + phoneNumber + "&udf4=" + bookingId;


            vVenderURL = vVenderURL.replace(" ", "%20");
            //Log.d(TAG, "First URL :-" +vVenderURL);

            try {

                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[]{
                        new X509TrustManager() {
                            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                            }

                            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                            }

                            public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[]{};
                            }
                        }
                }, null);

                HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());


                URL requestedUrl = new URL(vVenderURL);
                HttpURLConnection urlConnection = (HttpURLConnection) requestedUrl.openConnection();
                if (urlConnection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) urlConnection).setSSLSocketFactory(ctx.getSocketFactory());
                }

                urlConnection.setRequestMethod("GET");

//			    http://chariotsolutions.com/blog/post/https-with-client-certificates-on/
                BufferedReader inBuf = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                String vXMLStr = "";
                while ((inputLine = inBuf.readLine()) != null) {
                    //System.out.println(inputLine);
                    vXMLStr = vXMLStr + inputLine;
                }

                inBuf.close();
                System.out.println("Got Content");
                System.out.println(vXMLStr);


                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                InputSource isBuf = new InputSource();
                isBuf.setCharacterStream(new StringReader(vXMLStr));
                Document doc = dBuilder.parse(isBuf);
                doc.getDocumentElement().normalize();

                System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                NodeList nList = doc.getElementsByTagName("RESPONSE");

                for (int tempN = 0; tempN < nList.getLength(); tempN++) {
                    Node nNode = nList.item(tempN);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        System.out.println("URL : " + eElement.getElementsByTagName("url").item(0).getChildNodes().item(0).getNodeValue());
                        xmlURL = eElement.getElementsByTagName("url").item(0).getChildNodes().item(0).getNodeValue();

                        NodeList aList = eElement.getElementsByTagName("param");
                        String vParamName;
                        for (int atrCnt = 0; atrCnt < aList.getLength(); atrCnt++) {
                            vParamName = aList.item(atrCnt).getAttributes().getNamedItem("name").getNodeValue();
                            System.out.println("<br>paramName : : " + vParamName);

                            if (vParamName.equals("ttype")) {
                                xmlttype = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
                            } else if (vParamName.equals("tempTxnId")) {
                                xmltempTxnId = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
                            } else if (vParamName.equals("token")) {
                                xmltoken = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
                            } else if (vParamName.equals("txnStage")) {
                                xmltxnStage = aList.item(atrCnt).getChildNodes().item(0).getNodeValue();
                            }
                        }
                    }
                }

                Atom2Request = xmlURL + "?ttype=" + xmlttype + "&tempTxnId=" + xmltempTxnId + "&token=" + xmltoken + "&txnStage=" + xmltxnStage;

                Atom2Request = Atom2Request.replace(" ", "%20");
                //Log.d(TAG, "Second Request :-" +Atom2Request);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//		java.io.FileNotFoundException: https://paynetzuat.atomtech.in/paynetz/epi/fts?login=160&pass=Test@123&ttype=NBFundTransfer&prodid=NSE&amt=2701.0&txncurr=INR&txnscamt=0&clientcode=MTIz&txnid=144&date=15/09/2015%2011:38:18&custacc=888888888888&ru=http://192.168.0.122:8004/gateway_response_mobile/&udf1=ankitalahoti1992@gmail.com&udf2=
        return Atom2Request;
    }
}

