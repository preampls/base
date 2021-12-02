package com.topit.mylibrary.util;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XfsUtil {
	private static String addMapToRowElement(Map map) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			stringBuilder.append("<R>");
			Iterator keyValuePairs = map.entrySet().iterator();
			for (int i = 0; i < map.size(); i++) {
				Entry entry = (Entry) keyValuePairs.next();
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				if ((value instanceof List)) {
					stringBuilder.append("<C N=\"" + key + "\">");
					stringBuilder.append(addListToDbsetElement((List) value));
					stringBuilder.append("</C>");
				} else if ((value == null) || ((value instanceof String))) {
					String mapValue = value == null ? "" : (String) value;
					stringBuilder.append("<C N=\"" + key + "\">" + mapValue + "</C>");
				} else {
					stringBuilder.append("<C N=\"" + key + "\">" + value + "</C>");
				}
			}
			stringBuilder.append("</R>");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("解析成xml格式出错！");
		}
		return stringBuilder.toString();
	}

	private static String addListToDbsetElement(List srcList) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			stringBuilder.append("<DBSET>");
			Iterator it = srcList.iterator();
			while (it.hasNext()) {
				Map map = (Map) it.next();
				stringBuilder.append(addMapToRowElement(map));
			}
			stringBuilder.append("</DBSET>");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("解析成xml格式出错！");
		}
		return stringBuilder.toString();
	}

	public static String toXfs(Map<String, Object> map) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		stringBuilder.append("<DBSET>");
		stringBuilder.append(addMapToRowElement(map));
		stringBuilder.append("</DBSET>");

		return stringBuilder.toString();
	}

	public static List<Map<String, Object>> toList(String xmlStr) {
		List<Map<String, Object>> list = new ArrayList();
		xmlStr = xmlStr.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\f", "").replaceAll("\t", "");

		list = getStrR(xmlStr);
		return list;
	}

	public static List<Map<String, Object>> getStrR(String xmlStr) {
		List<String> strRList = new ArrayList();
		List<Map<String, Object>> mapList = new ArrayList();
		xmlStr = xmlStr.substring(xmlStr.indexOf("<DBSET"));
		String resultStr = xmlStr.substring(xmlStr.indexOf("\"") + 1, xmlStr.indexOf("\">"));
		int result = Integer.parseInt(resultStr);
		if (result == 0) {
			return mapList;
		}
		xmlStr = xmlStr.substring(xmlStr.indexOf(">") + 1, xmlStr.lastIndexOf("</"));
		int strLength = xmlStr.length();
		int index = 3;
		for (int k = 0; k < result; k++) {
			StringBuilder strCBulder = new StringBuilder();
			if (index < strLength) {
				for (int i = 0; index < strLength; i++) {
					String strC = "";
					strC = xmlStr.substring(index, xmlStr.indexOf("/C>", index) + 3);
					if (strC.contains("DBSET")) {
						System.out.println(xmlStr.indexOf(strC));
						System.out.println(xmlStr.indexOf("</DBSET", xmlStr.indexOf(strC)));
						if (xmlStr.indexOf("</DBSET", xmlStr.indexOf(strC)) != -1) {
							strC = xmlStr.substring(xmlStr.indexOf(strC),
									xmlStr.indexOf("</DBSET", xmlStr.indexOf(strC))) + "</DBSET></C>";
						}
					}
					String preStr = strC.substring(0, 7);
					if ("</R><R>".equals(preStr)) {
						String ss = "";
						for (int j = 0; j < strRList.size(); j++) {
							String s = (String) strRList.get(j);
							ss = ss + s;
						}
						index = strCBulder.length() + ss.length();
						index += 10;
						String resultStrA = "<R>" + strCBulder.toString() + "</R>";
						strRList.add(resultStrA);
						break;
					}
					strCBulder.append(strC);
					String resultStrC = "<R>" + strCBulder.toString() + "</R>";
					String resultStrB = "<R>" + strCBulder.toString() + "</R>";
					String ss = "";
					for (int j = 0; j < strRList.size(); j++) {
						String s = (String) strRList.get(j);
						ss = ss + s;
					}
					resultStrC = ss + resultStrC;
					if (resultStrC.length() == strLength) {
						strRList.add(resultStrB);
						break;
					}
					index = xmlStr.indexOf(strCBulder.toString()) + strCBulder.length();
				}
			}
		}
		for (int i = 0; i < strRList.size(); i++) {
			Map<String, Object> mapC = getStrC((String) strRList.get(i));
			mapList.add(mapC);
		}
		return mapList;
	}

	public static Map<String, Object> getStrC(String xmlStr) {
		Map<String, Object> mapC = new HashMap();
		int strLength = xmlStr.length();
		int index = 3;
		StringBuilder strCBulder = new StringBuilder();
		if (index < strLength) {
			for (int i = 0; index < strLength; i++) {
				String strC = "";
				strC = xmlStr.substring(index, xmlStr.indexOf("/C>", index) + 3);
				if (strC.contains("DBSET")) {
					strC = xmlStr.substring(xmlStr.indexOf(strC), xmlStr.indexOf("</DBSET>", xmlStr.indexOf(strC)))
							+ "</DBSET></C>";
					String strCDBSET = strC.substring(strC.indexOf("<DBSET"), strC.lastIndexOf("</DBSET>") + 8);
					List<Map<String, Object>> list = getStrR(strCDBSET);
					String key = strC.substring(strC.indexOf("\"") + 1, strC.indexOf("\">"));
					mapC.put(key, list);
				} else {
					mapC = getMapData(strC, mapC);
				}
				strCBulder.append(strC);
				String resultStr = "<R>" + strCBulder.toString() + "</R>";
				if (resultStr.length() == strLength) {
					break;
				}
				index = xmlStr.indexOf(strCBulder.toString()) + strCBulder.length();
			}
		}
		return mapC;
	}

	public static Map<String, Object> getMapData(String xmlStr, Map<String, Object> map) {
		String key = xmlStr.substring(xmlStr.indexOf("\"") + 1, xmlStr.lastIndexOf("\""));
		String value = xmlStr.substring(xmlStr.indexOf("\">") + 2, xmlStr.lastIndexOf("<"));
		map.put(key, value);
		return map;
	}

	/**
	 * 
	 * 去掉xml version CDATA ,&lt;等
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static String formatRawXML(String xmlStr) {
		// 先取出CDATA的内容
		Pattern p = Pattern.compile("[\\s\\S]*<!\\[CDATA\\[([\\s\\S]*)\\]\\]>[\\s\\S]*");
		Matcher m = p.matcher(xmlStr);
		if (m.matches()) {
			xmlStr = m.group(1);
		}
		// 去掉头部
		String index = "?>";
		if (xmlStr.indexOf(index) >= 0) {
			xmlStr = xmlStr.substring(xmlStr.indexOf(index) + 2, xmlStr.length());
		}
		// 部分XML接口返回，还存在转义没处理的问题
		if (xmlStr.indexOf("&lt;") >= 0) {
			xmlStr = xmlStr.replaceAll("&lt;", "<");
		}
		if (xmlStr.indexOf("&gt;") >= 0) {
			xmlStr = xmlStr.replaceAll("&gt;", ">");
		}
		if (xmlStr.indexOf("&#xd;") >= 0) {
			xmlStr = xmlStr.replaceAll("&#xd;", "");
		}
		return xmlStr;
	}

	public static JSONArray jsonObject2JsonArray(Object obj) {
		if (obj != null) {
			JSONArray array = new JSONArray();
			array.put(obj);
			return array;
		} else {
			return new JSONArray();
		}
	}

}
