package cn.ggband.udp.bean;


import java.lang.reflect.Type;

import cn.ggband.udp.UdpCallBack;

public class CallbackParams {
    private UdpCallBack callBack;
    private long taskTime = System.currentTimeMillis();
    private Type returnType;


    public CallbackParams(UdpCallBack callBack, Type returnType) {
        this.callBack = callBack;
        this.returnType = returnType;
    }



    public UdpCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(UdpCallBack callBack) {
        this.callBack = callBack;
    }

    public long getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(long taskTime) {
        this.taskTime = taskTime;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }
}
