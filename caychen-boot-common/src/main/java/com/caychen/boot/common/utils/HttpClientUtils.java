package com.caychen.boot.common.utils;


import com.caychen.boot.common.constant.HttpConstant;
import com.caychen.boot.common.constant.CommonConstant;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Caychen
 * @Date: 2021/8/24 19:22
 * @Describe:
 */
public class HttpClientUtils {

    public static final int CONNECT_TIMEOUT = 10000;

    public static final int READ_TIMEOUT = 10000;

    private static final HttpClient client;

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(128);
        cm.setDefaultMaxPerRoute(128);
        client = HttpClients.custom().setConnectionManager(cm).build();
    }

    public static String postParameters(
            String url,
            String parameterStr)
            throws Exception {
        return post(url,
                parameterStr,
                HttpConstant.MIME_TYPE_FORM_URLENCODED,
                CommonConstant.DEFAULT_CHARSET,
                CONNECT_TIMEOUT,
                READ_TIMEOUT);
    }

    public static String postParameters(
            String url,
            String parameterStr,
            String charset,
            Integer connTimeout,
            Integer readTimeout)
            throws Exception {
        return post(url,
                parameterStr,
                HttpConstant.MIME_TYPE_FORM_URLENCODED,
                charset,
                connTimeout,
                readTimeout);
    }

    public static String postParameters(String url, Map<String, String> params) throws
            Exception {
        return postForm(url,
                params,
                null,
                CONNECT_TIMEOUT,
                READ_TIMEOUT);
    }

    public static String postParameters(
            String url,
            Map<String, String> params,
            Integer connTimeout,
            Integer readTimeout)
            throws Exception {
        return postForm(url,
                params,
                null,
                connTimeout,
                readTimeout);
    }

    public static String get(String url) throws Exception {
        return get(url,
                CommonConstant.DEFAULT_CHARSET,
                null,
                null);
    }

    public static String get(String url, String charset) throws Exception {
        return get(url,
                charset,
                CONNECT_TIMEOUT,
                READ_TIMEOUT);
    }

    /**
     * ???????????? Post ??????, ??????????????????????????????.
     *
     * @param url
     * @param body        RequestBody
     * @param mimeType    ?????? application/xml MimeTypeConstant.FORM_URLENCODED a=1&b=2&c=3
     * @param charset     ??????
     * @param connTimeout ????????????????????????,??????.
     * @param readTimeout ??????????????????,??????.
     * @return ResponseBody, ??????????????????????????????.
     * @throws ConnectTimeoutException ????????????????????????
     * @throws SocketTimeoutException  ????????????
     * @throws Exception
     */
    public static String post(String url,
                              String body,
                              String mimeType,
                              String charset,
                              Integer connTimeout,
                              Integer readTimeout)
            throws ConnectTimeoutException, SocketTimeoutException, Exception {
        HttpClient client = null;
        HttpPost post = new HttpPost(url);
        String result = "";
        try {
            if (StringUtils.isNotBlank(body)) {
                HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
                post.setEntity(entity);
            }
            // ????????????
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());

            HttpResponse res;
            if (url.startsWith("https")) {
                // ?????? Https ??????.
                client = createSSLInsecureClient();
                res = client.execute(post);
            } else {
                // ?????? Http ??????.
                client = HttpClientUtils.client;
                res = client.execute(post);
            }
            result = IOUtils.toString(res.getEntity().getContent(), charset);
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return result;
    }


    /**
     * ??????form??????
     *
     * @param url
     * @param params
     * @param connTimeout
     * @param readTimeout
     * @return
     * @throws ConnectTimeoutException
     * @throws SocketTimeoutException
     * @throws Exception
     */
    public static String postForm(
            String url,
            Map<String, String> params,
            Map<String, String> headers,
            Integer connTimeout,
            Integer readTimeout) throws Exception {

        HttpClient client = null;
        HttpPost post = new HttpPost(url);
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> formParams = Lists.newArrayList();
                Set<Map.Entry<String, String>> entrySet = params.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                post.setEntity(entity);
            }

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // ????????????
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            HttpResponse res = null;
            if (url.startsWith(HttpConstant.HTTPS)) {
                // ?????? Https ??????.
                client = createSSLInsecureClient();
                res = client.execute(post);
            } else {
                // ?????? Http ??????.
                client = HttpClientUtils.client;
                res = client.execute(post);
            }
            return IOUtils.toString(res.getEntity().getContent(), StandardCharsets.UTF_8);
        } finally {
            post.releaseConnection();
            if (url.startsWith(HttpConstant.HTTPS)
                    && client != null
                    && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
    }


    /**
     * ???????????? GET ??????
     *
     * @param url
     * @param charset
     * @param connTimeout ????????????????????????,??????.
     * @param readTimeout ??????????????????,??????.
     * @return
     * @throws ConnectTimeoutException ??????????????????
     * @throws SocketTimeoutException  ????????????
     * @throws Exception
     */
    public static String get(String url, String charset, Integer connTimeout, Integer readTimeout) throws Exception {

        HttpClient client = null;
        HttpGet get = new HttpGet(url);
        String result = "";
        try {
            // ????????????
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            get.setConfig(customReqConf.build());

            HttpResponse res = null;

            if (url.startsWith(HttpConstant.HTTPS)) {
                // ?????? Https ??????.
                client = createSSLInsecureClient();
                res = client.execute(get);
            } else {
                // ?????? Http ??????.
                client = HttpClientUtils.client;
                res = client.execute(get);
            }

            result = IOUtils.toString(res.getEntity().getContent(), charset);
        } finally {
            get.releaseConnection();
            if (url.startsWith(HttpConstant.HTTPS)
                    && client != null
                    && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return result;
    }


    /**
     * ??? response ????????? charset
     *
     * @param ressponse
     * @return
     */
    @SuppressWarnings("unused")
    private static String getCharsetFromResponse(HttpResponse ressponse) {
        // Content-Type:text/html; charset=GBK
        if (ressponse.getEntity() != null
                && ressponse.getEntity().getContentType() != null
                && ressponse.getEntity().getContentType().getValue() != null) {
            String contentType = ressponse.getEntity().getContentType().getValue();
            if (contentType.contains("charset=")) {
                return contentType.substring(contentType.indexOf("charset=") + 8);
            }
        }
        return null;
    }


    /**
     * ?????? SSL??????
     *
     * @return
     * @throws GeneralSecurityException
     */
    private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        try {
            SSLContext sslContext =
                    new SSLContextBuilder()
                            .loadTrustMaterial(null,
                                    (chain, authType) -> true
                            )
                            .build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, (arg0, arg1) -> true);

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();

        } catch (GeneralSecurityException e) {
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            String str = post(
                    "https://localhost:443/ssl/test.shtml",
                    "name=12&page=34",
                    HttpConstant.MIME_TYPE_FORM_URLENCODED,
                    "UTF-8",
                    10000,
                    10000);
            //String str= get("https://localhost:443/ssl/test.shtml?name=12&page=34","GBK");
            /*Map<String,String> map = new HashMap<String,String>();
            map.put("name", "111");
            map.put("page", "222");
            String str= postForm("https://localhost:443/ssl/test.shtml",map,null, 10000, 10000);*/
            System.out.println(str);
        } catch (ConnectTimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}