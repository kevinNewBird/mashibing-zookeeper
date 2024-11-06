package com.mashibing.zoo.util;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
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

    private static Logger logger = LoggerFactory.getLogger(DefaultWatcher.class);

    private CountDownLatch monitor;

    public DefaultWatcher(CountDownLatch monitor) {
        this.monitor = monitor;
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
                logger.info("zookeeper client  connected success...");
                monitor.countDown();
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
}
