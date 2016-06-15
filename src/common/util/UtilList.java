package common.util;
//package com.framework.util;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class UtilList extends org.apache.commons.collections.UtilList {
//
//	public static <T, S> List<T> transform(List<S> list, Class<T> clazz) {
//		List<T> result = new ArrayList<T>();
//		for (S s : list) {
//			try {
//				T o = clazz.newInstance();
//				PropertyUtils.copyProperties(o, s);
//				result.add(o);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	public static <S> List<Map<String, Object>> transform(List<S> list, String... fields) {
//		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//		for (S s : list) {
//			try {
//				Map<String, Object> map = new HashMap<String, Object>();
//				for (String field : fields) {
//					map.put(field, PropertyUtils.getProperty(s, field));
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
//}
