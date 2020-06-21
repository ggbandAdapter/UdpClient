package cn.ggband.udp.bean;

import java.lang.reflect.Type;

public class ParseParams {
    private byte[] data;
    private Type returnType;


    public ParseParams(byte[] data) {
        this.data = data;
    }


    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }
}
