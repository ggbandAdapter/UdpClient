package cn.ggband.udp.ac.bean;

/**
 * 扫描设备
 */
public class ScanMessage  {
    private String product;//产品型号
    private String sn;//设备sn
    private String ver;//软件版本
    private String mac;//设备mac
    private String type;//设备类型
    private String manage;//绑定账号若为空则未绑定
    private int port;//ucloud对app监听
    private String ip;

    private int cmd;//回应的指令编号 扫描为0绑定为1

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManage() {
        return manage;
    }

    public void setManage(String manage) {
        this.manage = manage;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "ScanMessage{" +
                "product='" + product + '\'' +
                ", sn='" + sn + '\'' +
                ", ver='" + ver + '\'' +
                ", mac='" + mac + '\'' +
                ", type='" + type + '\'' +
                ", manage='" + manage + '\'' +
                ", port=" + port +
                ", ip='" + ip + '\'' +
                '}';
    }
}
