package com.mashibing.zoo.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * description  ZookeeperUtil <BR>
 * <p>
 * author: zhao.song
 * date: created in 11:37  2022/3/12
 * company: TRS信息技术有限公司
 * version 1.0
 */
public final class ZookeeperUtil {

    private static volatile ZooKeeper zk;


    private static final String SERVER_ADDRESS;

    // 默认的配置文件的父目录, 这种配置方式，如果在调用zk.create（‘/app’）,完整的路径应为 /testConf/app
    private static final String DEFAULT_ROOT_DIR = "/";

    private static final int SESSION_TIMEOUT = 3_000;

    static {
        String os = System.getProperty("os.name");
        if (StringUtils.containsIgnoreCase(os, "windows")) {
            SERVER_ADDRESS = "172.22.124.60:2281,172.22.124.60:2282,172.22.124.60:2283";
        } else { // mac
            SERVER_ADDRESS = "10.211.55.13:2281,10.211.55.13:2282,10.211.55.13:2283";
        }
    }

    public static ZooKeeper getConnection() {
        return getConnection(DEFAULT_ROOT_DIR);
    }

    public static ZooKeeper getConnection(String rootDir) {

        try {
            // 控制器，用于确保zk连接成功（基于监听事件）
            CountDownLatch monitor = new CountDownLatch(1);
            // 没有使用单例！因为zookeeper是基于会话来建立客户端的，连接一旦释放，会话也就结束了
            zk = new ZooKeeper(SERVER_ADDRESS + rootDir, SESSION_TIMEOUT, new DefaultWatcher(monitor));
            monitor.await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return zk;
    }

    public static void close() throws InterruptedException {
        zk.close();
    }

}
