package cn.ggband.udp;

public interface Call<T> {
    void send(UdpCallBack<T> callback);
}
