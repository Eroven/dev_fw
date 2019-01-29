package me.zhaotb.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CollectionUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CollectionUtil.class);
	
	public static boolean isEmpty(Collection c) {
		return c == null || c.size() < 1;
	}
	
	public static boolean isNotEmpty(Collection c) {
		return !isEmpty(c);
	}
	
	/**
	 * 将src前size个值划分给target,剩余的值作为一个新list返回
	 * @param src
	 * @param target
	 * @param size
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> split(List<T> src, List<T> target, int size) {
		if(target == null) {
			logger.error("目标List为空!");
			throw new NullPointerException("目标List为空!");
		}
		if(size == 0) {
			ArrayList<T> list = new ArrayList<T>();
			list.addAll(src);
			return list;
		}
		if(src.size() <= size) {
			target.addAll(src);
			return Collections.EMPTY_LIST;
		}
		target.addAll(src.subList(0, size-1));
		return src.subList(size, src.size()-1);
	}

	public static List<String> toUpperCase(List<String> list){
		if(CollectionUtil.isEmpty(list)){
			return list;
		}
		ArrayList<String> upperList = new ArrayList<String>(list.size());
		for (String s : list){
			if(StringUtil.isNotEmpty(s)){
				s = s.toUpperCase();
			}
			upperList.add(s);
		}
		return upperList;
	}

}
