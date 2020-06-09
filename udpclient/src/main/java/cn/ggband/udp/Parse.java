package cn.ggband.udp;

import com.google.gson.Gson;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.ggband.udp.anno.Field;
import cn.ggband.udp.anno.Task;
import cn.ggband.udp.bean.ParseParams;

public class Parse {

    public static <T> void validateServiceInterface(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        // Prevent API interfaces from extending other interfaces. This not only avoids a bug in
        // Android (http://b.android.com/58753) but it forces composition of API declarations which is
        // the recommended pattern.
        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
    }


    /**
     * 构建参数
     *
     * @param method 方法
     * @param args   参数
     * @return 参数
     */
    public static ParseParams parseParameter(Method method, Object[] args) {

        Class[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int parameterTypesLength = parameterTypes.length;
        if (parameterTypesLength != 2) {
            throw new IllegalArgumentException("annotation error");
        }
        Object[] values = (Object[]) args[0];

        //参数结果
        Map<Object, Object> resultMap = new HashMap<>();

        Annotation[] targetAnnotations = new Annotation[parameterAnnotations.length];

        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation annotation = getParameterAnnotation(i, parameterAnnotations);
            targetAnnotations[i] = annotation;
        }
        String taskId = "";
        byte[] data = "".getBytes();
        for (int i = 0; i < parameterTypesLength; i++) {

            if (targetAnnotations[i] instanceof Task) {
                //   TaskId taskIdAnno = (TaskId) targetAnnotations[i];
                taskId = values[i].toString();
            } else if (targetAnnotations[i] instanceof Field) {
                //  Field field = (Field) targetAnnotations[i];
                if (parameterTypes[i] == byte[].class) {
                    data = (byte[]) values[i];
                } else if (parameterTypes[i] == String.class) {
                    data = values[i].toString().getBytes();
                } else {
                    data = new Gson().toJson(values[i]).getBytes();
                }
            }
        }
        return new ParseParams(taskId, data);
    }


    /**
     * 获取API方法的注解
     *
     * @param index            方法上第几个参数
     * @param annotationCounts 方法的注解集合
     * @return 业务自身的api注解
     */
    private static Annotation getParameterAnnotation(int index, Annotation[][] annotationCounts) {
        Annotation[] annotations = annotationCounts[index];
        Annotation targetAnnotation = null;
        for (Annotation annotation : annotations) {
            if (annotation instanceof Field) {
                targetAnnotation = annotation;
                break;
            } else if (annotation instanceof Task) {
                targetAnnotation = annotation;
                break;
            }
        }

        return targetAnnotation;

    }


}
