package cn.ggband.udp;



import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetAddress;

import cn.ggband.udp.bean.ParseParams;

public class Platform {

    private UdpClient mClient;

    private Platform(UdpClient client) {
        this.mClient = client;
    }

    public static Platform get(UdpClient client) {
        return new Platform(client);
    }

    @Nullable
    public Object invokeDefaultMethod(Method method, Class<?> declaringClass, Object object,
                                      @Nullable Object... args) {

        // 获取返回值类型
        Type type = method.getGenericReturnType();
        // 获取返回值类型 name
        Class returnClass = method.getReturnType();
        //返回类型不能处理抛出返回类型错误异常
        if (returnClass.getName().equals("void")) {
            return null;
        } else if (returnClass != Call.class) {
            throw new RuntimeException("Return value type must be" + Call.class.getName());
        }


        Type returnType = null;
        //  type instanceof Class 没有解析类型 Call<*>/Call
        if (type instanceof ParameterizedType) {
            // 判断获取的类型是否是参数类型;
            // 强制转型为带参数的泛型类型，
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            // getActualTypeArguments()方法获取类型中的实际类型，如map<String,Integer>中的
            // String，integer因为可能是多个，所以使用数组
            //Call的 actualTypeArguments 最多只有一个 Call<*>
            if (types.length > 0) {
                returnType = types[0];
            }
        }
        ParseParams params = Parse.parseParameter(method, args);
        params.setReturnType(returnType);
        return new ProxyCall(params);
    }


    /**
     * api 回调代理
     */
    private class ProxyCall implements Call<Object> {

        private ParseParams params;

        ProxyCall(ParseParams params) {
            this.params = params;
        }

        @Override
        public void send(final UdpCallBack<Object> callback) {
            mClient.send(params.getTaskId(), params.getData(), new UdpCallBack<Object>() {
                @Override
                public void onReceiveDone() {
                    callback.onReceiveDone();
                }

                @Override
                public void onReceive(Object data, InetAddress address) {
                    callback.onReceive(data, address);
                }
            }, params.getReturnType());
        }
    }
}
