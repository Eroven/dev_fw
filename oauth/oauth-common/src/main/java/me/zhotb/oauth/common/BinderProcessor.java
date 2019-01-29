package me.zhotb.oauth.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;

/**
 * @author zhaotangbo
 * @date 2018/11/23
 */
public class BinderProcessor extends ServletModelAttributeMethodProcessor implements ApplicationContextAware {

    private ApplicationContext context;

    public BinderProcessor(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        super.bindRequestParameters(binder, request);
        AnnotationBinder dataBinder = new AnnotationBinder(binder.getTarget(), binder.getObjectName());
        RequestMappingHandlerAdapter adapter = context.getBean(RequestMappingHandlerAdapter.class);
        adapter.getWebBindingInitializer().initBinder(dataBinder);
        dataBinder.bind(request.getNativeRequest(ServletRequest.class));
    }
}
