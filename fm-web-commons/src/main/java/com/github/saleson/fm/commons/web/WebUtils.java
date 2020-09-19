package com.github.saleson.fm.commons.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.*;

public class WebUtils {

    public static final String CHARSET_UTF8 = "UTF-8";

    private static final Logger LOG = LoggerFactory.getLogger(WebUtils.class);

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @param encode 是否需要urlEncode
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean encode) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode) {
                try {
                    value = URLEncoder.encode(value, CHARSET_UTF8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    public static String toQueryString(Map<String, String> params) {
        return toQueryString(params, false);
    }


    /**
     * 将params转换成url query string
     *
     * @param params
     * @return
     */
    public static String toQueryString(Map<String, String> params, boolean urlEncoding) {
        StringBuilder str = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (str.length() > 0) {
                    str.append("&");
                }
                String entryValue = StringUtils.defaultString(entry.getValue());
                String value = urlEncoding ? urlEncoding(entryValue) : entryValue;
                str.append(entry.getKey()).append("=").append(value);
            }
        }
        return str.toString();
    }

    /**
     * queryString转Json
     *
     * @param queryStr example: jkd=32&32=&23
     * @return
     */
    public static String queryStringToJson(String queryStr) {
        if (StringUtils.isEmpty(queryStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder("{");
        String[] entrys = queryStr.split("&");
        for (String entry : entrys) {
            if (StringUtils.isEmpty(entry)) {
                continue;
            }
            if (sb.length() > 1) {
                sb.append(",");
            }
            int len = entry.indexOf("=");
            if (len == -1) {
                sb.append("\"").append(entry).append("\":").append("null");
            } else {
                String key = entry.substring(0, len);
                String value = entry.substring(len + 1, entry.length());
                sb.append("\"").append(key).append("\":\"").append(value).append("\"");
            }
        }

        return sb.append("}").toString();
    }

    /**
     * 返回指定字符串url编码后的字符串
     *
     * @param str
     * @return
     */
    public static String urlEncoding(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, CHARSET_UTF8);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return str;
        }
    }

    /**
     * 返回指定字符串url解码后的字符串
     *
     * @param str
     * @return
     */
    public static String urlDecoding(String str) {
        if (str == null) {
            return null;
        }
        try {
            return URLDecoder.decode(str, CHARSET_UTF8);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return str;
        }
    }

    /**
     * queryString转Map
     *
     * @param queryStr example: jkd=32&32=&23
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> queryString2Map(String queryStr) {
        if (StringUtils.isEmpty(queryStr)) {
            return Collections.EMPTY_MAP;
        }
        Map<String, String> params = new HashMap<>();
        String[] entrys = queryStr.split("&");
        for (String entry : entrys) {
            if (StringUtils.isEmpty(entry)) {
                continue;
            }
            int len = entry.indexOf("=");
            if (len == -1) {
                params.put(entry, null);
            } else {
                String key = entry.substring(0, len);
                String value = entry.substring(len + 1, entry.length());
                params.put(key, value);
            }
        }

        return params;
    }


    /**
     * url query 转换成map
     *
     * @param urlQuery url query信息,如"a=1&amp;b=2&amp;c=3"
     * @return 转换后的map
     */
    public static Map<String, List<String>> getQueryParams(String urlQuery) {
        Map<String, List<String>> qp = new HashMap<String, List<String>>();

        if (urlQuery == null) return qp;
        StringTokenizer st = new StringTokenizer(urlQuery, "&");
        int i;

        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            i = s.indexOf("=");
            if (i > 0 && s.length() >= i + 1) {
                String name = s.substring(0, i);
                String value = s.substring(i + 1);

                try {
                    name = URLDecoder.decode(name, "UTF-8");
                } catch (Exception e) {
                }
                try {
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (Exception e) {
                }

                List<String> valueList = qp.get(name);
                if (valueList == null) {
                    valueList = new LinkedList<String>();
                    qp.put(name, valueList);
                }

                valueList.add(value);
            } else if (i == -1) {
                String name = s;
                String value = "";
                try {
                    name = URLDecoder.decode(name, "UTF-8");
                } catch (Exception e) {
                }

                List<String> valueList = qp.get(name);
                if (valueList == null) {
                    valueList = new LinkedList<String>();
                    qp.put(name, valueList);
                }

                valueList.add(value);

            }
        }
        return qp;
    }

    /**
     * 组装url参数
     *
     * @param url
     * @param params
     * @param urlEncoding 是否url编码
     * @return
     */
    public static String completionUrlParams(String url, Map<String, String> params, boolean urlEncoding) {
        String queryStr = toQueryString(params, urlEncoding);
        return completionUrlParams(url, queryStr);
    }

    /**
     * 组装url参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String completionUrlParams(String url, String params) {
        StringBuilder str = new StringBuilder();
        str.append(url).append(StringUtils.indexOf(url, "?") != -1 ? "&" : "?").append(params);
        return str.toString();
    }

    /**
     * <p>
     * 获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。
     * 但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了，如果通过了多级反向代理的话，
     * X-Forwarded-For的值并不止一个，而是一串IP值， 究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 例如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100 用户真实IP为： 192.168.1.110
     * </p>
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                /** 根据网卡取本机配置的IP */
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    LOG.error("[IpHelper-getIpAddr] IpHelper error.", e);
                }
            }
        }
        /**
         * 对于通过多个代理的情况， 第一个IP为客户端真实IP,多个IP按照','分割 "***.***.***.***".length() =
         * 15
         */
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

}
