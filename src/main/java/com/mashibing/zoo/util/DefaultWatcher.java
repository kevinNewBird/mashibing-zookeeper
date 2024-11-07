package com.mashibing.zoo.util;

import com.mashibing.zoo.callback.DataCallbackTest;
import com.mashibing.zoo.callback.StatCallbackTest;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * description  DefaultWatch <BR>
 * <p>
 * author: zhao.song
 * date: created in 15:30  2022/3/13
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class DefaultWatcher implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(DefaultWatcher.class);

    private final CountDownLatch monitor;

    private ZooKeeper zk;

    public DefaultWatcher(CountDownLatch monitor) {
        this.monitor = monitor;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getState()) {
            case Unknown:
                break;
            case Disconnected:
                break;
            case NoSyncConnected:
                break;
            case SyncConnected:
                doSyncConnected(event);
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
    }

    /**
     * description:监听同步连接状态
     * create by: zhaosong 2024/11/7 16:17
     *
     * @param event
     */
    private void doSyncConnected(WatchedEvent event) {
        try {
            if (Event.EventType.None == event.getType() && null == event.getPath()) { // 1.新建连接时无事件且路径为空
                logger.info("zookeeper client  connected success...");
                monitor.countDown();
            } else if (event.getType() == Event.EventType.NodeDataChanged) { // 2.监听更新数据
                doGetDataBySync(event);
                System.out.println("Node(" + event.getPath() + ")DataChanged");
                zk.exists(event.getPath(), true);
            } else if (event.getType() == Event.EventType.NodeCreated) {
                System.out.println("Node(" + event.getPath() + ")Created");
                zk.exists(event.getPath(), true);
            } else if (event.getType() == Event.EventType.NodeDeleted) {
                System.out.println("Node(" + event.getPath() + ")Deleted");
                zk.exists(event.getPath(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步获取数据变化信息
     * description:
     * create by: zhaosong 2024/11/7 16:39
     *
     * @param event
     * @throws InterruptedException
     * @throws KeeperException
     */
    private void doGetDataBySync(WatchedEvent event) throws InterruptedException, KeeperException {
        Stat stat = new Stat(); // 用于接收状态信息
        byte[] data = zk.getData(event.getPath(), true, stat);
        System.err.printf("data of the %s had changed, result: %s, %s, %s, %s%n", event.getPath(), new String(data)
                , stat.getCzxid(), stat.getMzxid(), stat.getPzxid());
    }

    /**
     * 异步获取数据变化信息
     * description:
     * create by: zhaosong 2024/11/7 16:39
     *
     * @param event
     * @throws InterruptedException
     * @throws KeeperException
     */
    private void doGetDataByAsync(WatchedEvent event) throws InterruptedException, KeeperException {
        zk.getData(event.getPath(), true, new DataCallbackTest(), null);
    }
}
