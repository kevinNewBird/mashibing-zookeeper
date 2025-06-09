package com.mashibing.zoo;

import com.mashibing.zoo.service.SyncZKService;
import com.mashibing.zoo.util.ZookeeperUtil;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * description  ZKTests <BR>
 * <p>
 * author: zhao.song
 * date: created in 8:54  2022/3/11
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class ZKIpAuthTests {


    public static void main(String[] args) throws Exception {
        // zk是有session概念的，没有连接池的概念
        /**
         * watch（回调）分类两类(注册只发生在读类型调用，如get/exists)：
         * 第一类：new zk时，传入的watch,这个watch是session级别的，和path、node没有关系
         */

        final ZooKeeper zooKeeper = ZookeeperUtil.getConnection();
        final ZooKeeper.States state = zooKeeper.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("connecting...");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("connected");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }

        // 2.增删改查
        testSyncByIpAuth(zooKeeper);

        // 阻塞, 避免zk会话断开，从而导致临时目录删除
        System.in.read();

    }

    public static void testSyncByIpAuth(ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        String path = "/iptest";
        SyncZKService.createSync(zooKeeper,path);// 阻塞创建
        SyncZKService.getDataSync(zooKeeper,path);// 同步获取数据
//        SyncZKService.deleteSync(zooKeeper,path);
    }
}
