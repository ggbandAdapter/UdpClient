package cn.ggband.udp.bean;

import java.lang.reflect.Type;

public class ParseParams {
    private String taskId;
    private byte[] data;
    private Type returnType;


    public ParseParams(String taskId, byte[] data) {
        this.taskId = taskId;
        this.data = data;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
