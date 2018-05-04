package cn.bupt.device.sendEmailMethod;

import cn.bupt.device.pluginmanager.Constant;
import org.I0Itec.zkclient.ZkClient;

public class testZK {
    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("10.108.218.108:2181", Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
    }
}
