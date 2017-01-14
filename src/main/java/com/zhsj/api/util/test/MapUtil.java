package com.zhsj.api.util.test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MapUtil
{
	private static final Logger logger = LoggerFactory.getLogger(HttpsClientUtil.class);
	/**
	 * 过滤请求报文中的空字符串或者空字符串
	 * 
	 * @param contentData
	 * @return
	 */
	public static Map<String, String> filterBlank(Map<String, String> contentData)
	{
		Map<String, String> submitFromData = new HashMap<String, String>();
		Set<String> keyset = contentData.keySet();

		for (String key : keyset)
		{
			String value = contentData.get(key);
			if (StringUtils.isNotBlank(value))
			{
				// 对value值进行去除前后空处理
				submitFromData.put(key, value.trim());
			}
		}
		return submitFromData;
	}

	/**
	 * 将Map中的数据转换成按照Key的ascii码排序后的key1=value1&key2=value2的形式 不包含签名域sign
	 * 
	 * @param data
	 *            待拼接的Map数据
	 * @return 拼接好后的字符串
	 */
	public static String coverMap2String(Map<String, String> data)
	{
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, String> en = it.next();
			if (StaticConfig.PARAMS_SIGN.equals(en.getKey().trim()))
			{
				continue;
			}
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext())
		{
			Entry<String, String> en = it.next();
			sf.append(en.getKey() + StaticConfig.EQUAL + en.getValue() + StaticConfig.AMPERSAND);
		}
		return sf.substring(0, sf.length() - 1);
	}

	/**
	 * 将Map存储的对象，转换为key=value&key=value的字符
	 *
	 * @param requestParam
	 * @param coder
	 * @return
	 */
	public static String getRequestParamString(Map<String, String> requestParam)
	{
		StringBuffer sf = new StringBuffer("");
		String reqstr = "";
		if (null != requestParam && 0 != requestParam.size())
		{
			for (Entry<String, String> en : requestParam.entrySet())
			{
				try
				{
					sf.append(en.getKey() + "="
							+ (null == en.getValue() || "".equals(en.getValue()) ? "" : en.getValue()) + "&");
				} catch (Exception e)
				{
					logger.error(e.getMessage());
					return "";
				}
			}
			reqstr = sf.substring(0, sf.length() - 1);
		}
		logger.info("请求报文[包含返回验签SIGN]:" + reqstr + "");
		return reqstr;
	}

	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 *            需转换的字符串
	 * @return 转换完成后的Map对象
	 */
	public static Map<String, String> convertResultStringToMap(String result)
	{
		logger.info("返回报文[包含返回验签SIGN]：" + result);
		Map<String, String> map = null;
		try
		{

			if (StringUtils.isNotBlank(result))
			{
				if (result.startsWith("{") && result.endsWith("}"))
				{
					result = result.substring(1, result.length() - 1);
				}
				map = parseQString(result);
			}

		} catch (UnsupportedEncodingException e)
		{
			logger.info(e.getMessage());
		}
		return map;
	}

	/**
	 * 解析应答字符串，生成应答要素
	 * 
	 * @param str
	 *            需要解析的字符串
	 * @return 解析的结果map
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> parseQString(String str) throws UnsupportedEncodingException
	{

		Map<String, String> map = new HashMap<String, String>();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		boolean isOpen = false;// 值里有嵌套
		char openName = 0;
		if (len > 0)
		{
			for (int i = 0; i < len; i++)
			{// 遍历整个带解析的字符串
				curChar = str.charAt(i);// 取当前字符
				if (isKey)
				{// 如果当前生成的是key

					if (curChar == '=')
					{// 如果读取到=分隔符
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else
					{
						temp.append(curChar);
					}
				} else
				{// 如果当前生成的是value
					if (isOpen)
					{
						if (curChar == openName)
						{
							isOpen = false;
						}

					} else
					{// 如果没开启嵌套
						if (curChar == '{')
						{// 如果碰到，就开启嵌套
							isOpen = true;
							openName = '}';
						}
						if (curChar == '[')
						{
							isOpen = true;
							openName = ']';
						}
					}
					if (curChar == '&' && !isOpen)
					{// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
						putKeyValueToMap(temp, isKey, key, map);
						temp.setLength(0);
						isKey = true;
					} else
					{
						temp.append(curChar);
					}
				}

			}
			putKeyValueToMap(temp, isKey, key, map);
		}
		return map;
	}

	private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map)
			throws UnsupportedEncodingException
	{
		if (isKey)
		{
			key = temp.toString();
			if (key.length() == 0)
			{
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, "");
		} else
		{
			if (key.length() == 0)
			{
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, temp.toString());
		}
	}

}
