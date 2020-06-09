package cn.ggband.udp.ac.bean;


/**
 * 扫描设备
 */
public class ScanPackage  {
    private int cmd = 0x00;//命令
    private int udp_port = 11000;//Ac监听的组播端口
    private int src = 1;//消息来源 0---软AC ；1---APP

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getUdp_port() {
        return udp_port;
    }

    public void setUdp_port(int udp_port) {
        this.udp_port = udp_port;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }
}
