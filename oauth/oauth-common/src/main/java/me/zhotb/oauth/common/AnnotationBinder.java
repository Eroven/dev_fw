package me.zhotb.oauth.common;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaotangbo
 * @date 2018/11/23
 */
public class AnnotationBinder extends ExtendedServletRequestDataBinder {

    private ReentrantLock lock = new ReentrantLock();
    private HashMap<String, Field[]> fieldsCache = new HashMap<>();

    public AnnotationBinder(Object target) {
        super(target);
    }

    public AnnotationBinder(Object target, String objectName) {
        super(target, objectName);
    }

    @Override
    protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
        super.addBindValues(mpvs, request);

        Field[] fields = getFields();
        for (Field f : fields) {
            Binder binder = f.getAnnotation(Binder.class);
            //包含注解中的值, 但是不包含字段名字
            if (binder != null && mpvs.contains(binder.value())
                    && !mpvs.contains(f.getName())){
                mpvs.add(f.getName(), mpvs.getPropertyValue(binder.value()).getValue());
            }
        }

    }

    private Field[] getFields() {
        Class<?> tClass = getTarget().getClass();
        String name = tClass.getName();
        Field[] fields = fieldsCache.get(name);
        if (fields == null) {
            lock.lock();
            try {
                fields = fieldsCache.get(name);
                if (fields == null) {
                    fields = tClass.getDeclaredFields();
                    fieldsCache.putIfAbsent(name, fields);
                }
            }finally {
                lock.unlock();
            }
        }
        return fields;
    }
}
