package com.jinter.kit;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author clark
 * 
 *         Dec 29, 2016
 */
public class PropKit {
	
	private static Map<String, Map<Object, Object>> holdPropKV = new ConcurrentHashMap<String, Map<Object, Object>>();

	private static InputStream inputStream;
	private static Properties prop;

	/**
	 * using fileName to find property key's value . for example :
	 * <code> PropKit.use("conf/Jinter.properties", "user") <code>
	 * 
	 * @param fileName
	 * @param key
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object use(String fileName, String key) {

		Object retVal = find(fileName, key);
		if (retVal != null) {
			return retVal;
		}
		Object ret = null;

		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
			if (url == null) {
				throw new RuntimeException("can not find resouce " + fileName);
			}
			inputStream = url.openStream();
			prop = new Properties();
			prop.load(inputStream);
			Enumeration<Object> enums = prop.keys();
			Map stoleMap = new HashMap();
			while (enums.hasMoreElements()) {
				Object k = enums.nextElement();
				Object v = prop.get(k);
				if (k.equals(key)) {
					ret = v;
				}
				stoleMap.put(k, v);
			}
			holdPropKV.put(fileName, stoleMap);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		return ret;
	}

	/**
	 * find properties from specific file
	 * 
	 * @param fileName
	 * @param key
	 * @return
	 */
	public static Object find(String fileName, String key) {

		Map m = ((Map) (holdPropKV.get(fileName)));
		
		if (m != null) {
			return m.get(key);
		}
		
		return null;
	}

	/**
	 * find properties from all file that has been stoled . if there are more
	 * then one properties that match the key the first value will be returned
	 * 
	 * @param key
	 * @return
	 */
	public static Object find(String key) {

		Set<String> keys = holdPropKV.keySet();

		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String fileName = it.next();
			Map m = holdPropKV.get(fileName);
			Object v = m.get(key);
			if (v != null) {
				return v;
			}
		}

		return null;
	}
	
}
