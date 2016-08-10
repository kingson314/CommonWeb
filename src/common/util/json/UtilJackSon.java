package common.util.json;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @Description: J
 * @date Feb 13, 2014
 * @author:fgq
 */
public class UtilJackSon {

	private static ObjectMapper objectMapper = null;

	private UtilJackSon() {
		// 设置默认日期格式
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		// 提供其它默认设置
		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		// hibernate lazy代表属性，需要：
		// mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,
		// false)
		// 或排除"hibernateLazyInitializer","handler"属性。
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
	}
	public static ObjectMapper getObjectMapper() {
		if (objectMapper == null)
			objectMapper = new ObjectMapper();
		return objectMapper;
	}

	/**
	 * 将对象转换成json字符串格式（默认将转换所有的属性）
	 * @param o
	 * @return
	 */
	public static String toJson(Object value) throws JsonGenerationException, JsonMappingException, IOException {
		return getObjectMapper().writeValueAsString(value);
	}
	/**
	 * 将对象转换成json字符串格式
	 * 
	 * @param value
	 *            需要转换的对象(注意，需要在要转换的对象中定义JsonFilter注解)
	 * @param properties
	 *            需要转换的属性
	 */
	public String toJson(Object value, String[] properties) throws JsonGenerationException, JsonMappingException, IOException {
		return getObjectMapper().writer(
				new SimpleFilterProvider().addFilter(AnnotationUtils.getValue(AnnotationUtils.findAnnotation(value.getClass(), JsonFilter.class)).toString(), SimpleBeanPropertyFilter
						.filterOutAllExcept(properties))).writeValueAsString(value);
	}

	/**
	 * 将对象转换成json字符串格式
	 * @param value
	 *            需要转换的对象(注意，需要在要转换的对象中定义JsonFilter注解)
	 * @param properties2Exclude
	 *            需要排除的属性
	 */
	public String toJsonWithExcludeProperties(Object value, String[] properties2Exclude) throws JsonGenerationException, JsonMappingException, IOException {
		return getObjectMapper().writer(
				new SimpleFilterProvider().addFilter(AnnotationUtils.getValue(AnnotationUtils.findAnnotation(value.getClass(), JsonFilter.class)).toString(), SimpleBeanPropertyFilter
						.serializeAllExcept(properties2Exclude))).writeValueAsString(value);

	}

	/**
	 * 将对象json格式直接写出到流对象中（默认将转换所有的属性）
	 * 
	 * @param o
	 * @return
	 */
	public void writeJson(OutputStream out, Object value) throws JsonGenerationException, JsonMappingException, IOException {
		getObjectMapper().writeValue(out, value);
	}

	/**
	 * 将对象json格式直接写出到流对象中
	 * 
	 * @param value
	 *            需要转换的对象(注意，需要在要转换的对象中定义JsonFilter注解)
	 * @param properties
	 *            需要转换的属性
	 */
	public void writeJson(OutputStream out, Object value, String[] properties) throws JsonGenerationException, JsonMappingException, IOException {
		getObjectMapper().writer(
				new SimpleFilterProvider().addFilter(AnnotationUtils.getValue(AnnotationUtils.findAnnotation(value.getClass(), JsonFilter.class)).toString(), SimpleBeanPropertyFilter
						.filterOutAllExcept(properties))).writeValue(out, value);
	}

	/**
	 * 将对象转换成json字符串格式
	 * 
	 * @param value
	 *            需要转换的对象
	 * @param properties2Exclude
	 *            需要排除的属性(注意，需要在要转换的对象中定义JsonFilter注解)
	 */
	public void writeJsonWithExcludeProperties(OutputStream out, Object value, String[] properties2Exclude) throws JsonGenerationException, JsonMappingException, IOException {
		getObjectMapper().writer(
				new SimpleFilterProvider().addFilter(AnnotationUtils.getValue(AnnotationUtils.findAnnotation(value.getClass(), JsonFilter.class)).toString(), SimpleBeanPropertyFilter
						.serializeAllExcept(properties2Exclude))).writeValue(out, value);
	}
	
	/**
	 * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
	 * (1)转换为普通JavaBean：readValue(json,Student.class)
	 * (2)转换为List,如List<Student>,将第二个参数传递为Student
	 * [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
	 * 
	 * @param jsonStr
	 * @param valueType
	 * @return
	 */
	public static <T> T toBean(String jsonStr, Class<T> valueType) {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}

		try {
			return objectMapper.readValue(jsonStr, valueType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * json数组转List
	 * @param jsonStr
	 * @param valueTypeRef
	 * @return
	 */
	public static <T> T toObj(String jsonStr, TypeReference<T> valueTypeRef){
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();
		}

		try {
			return objectMapper.readValue(jsonStr, valueTypeRef);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}