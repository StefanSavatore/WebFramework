package com.qiu221b.framework.helper;

import com.qiu221b.framework.annotation.Action;
import com.qiu221b.framework.bean.Handler;
import com.qiu221b.framework.bean.Request;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControllerHelper {
    private  static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

    static{
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if(CollectionUtils.isNotEmpty(controllerClassSet)){
            for(Class<?> controllerClass : controllerClassSet){
                Method[] methods = controllerClass.getMethods();
                if(ArrayUtils.isNotEmpty(methods)){
                    for(Method method : methods){
                        if(method.isAnnotationPresent(Action.class)){
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            if(mapping.matches("\\w+:/\\w*")){
                                String[] array = mapping.split(":");
                                String requestMethod = array[0];
                                String requestPath = array[1];
                                Request request = new Request(requestMethod, requestPath);
                                Handler handler = new Handler(controllerClass, method);
                                ACTION_MAP.put(request, handler);
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String requestPath){
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}

