package com.mashibing.zoo;

import com.mashibing.zoo.service.AsyncZKService;
import com.mashibing.zoo.service.SyncZKService;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * description  ZKTests <BR>
 * <p>
 * author: zhao.song
 * date: created in 8:54  2022/3/11
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class ZKTests {

    static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        // zk是有session概念的，没有连接池的概念
        /**
         * watch（回调）分类两类(注册只发生在读类型调用，如get/exists)：
         * 第一类：new zk时，传入的watch,这个watch是session级别的，和path、node没有关系
         */

        final ZooKeeper zooKeeper = new ZooKeeper(
                "192.168.233.128:2181" +
                        ",192.168.233.132:2181" +
                        ",192.168.233.134:2181" +
                        ",192.168.233.133:2181"
                // 客户端断开连接，session会话的保持时间
                , 3000
                , new CreateWatcher());
        // 防止上面的异步执行
        latch.await();
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
//        testSync(zooKeeper);// 同步
        testAsync(zooKeeper);// 异步


        // 阻塞
        System.in.read();

    }


    public static void testSync(ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        SyncZKService.createSync(zooKeeper);// 阻塞创建
        SyncZKService.getDataSync(zooKeeper);// 同步获取数据
    }

    public static void testAsync(ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        AsyncZKService.createAsync(zooKeeper);// 异步回调创建,这个如果和createSync同时使用，会导致获取数据问题
        AsyncZKService.getDataAsync(zooKeeper); // 异步获取数据
    }


    public static class CreateWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            final Event.KeeperState state = event.getState();
            final Event.EventType type = event.getType();
            final String path = event.getPath();
            System.out.println("new zk" + event.toString());
            switch (state) {
                case Unknown:
                    break;
                case Disconnected:
                    break;
                case NoSyncConnected:
                    break;
                case SyncConnected:
                    System.out.println("watch connected");
                    latch.countDown();
                    break;
                case AuthFailed:
                    break;
                case ConnectedReadOnly:
                    break;
                case SaslAuthenticated:
                    break;
                case Expired:
                    break;
            }

            switch (type) {
                case None:
                    break;
                case NodeCreated:
                    break;
                case NodeDeleted:
                    break;
                case NodeDataChanged:
                    break;
                case NodeChildrenChanged:
                    break;
            }
        }
    }
}
