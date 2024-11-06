package com.mashibing.zoo.lock;

import com.mashibing.zoo.util.ZookeeperUtil;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * description：TODO
 *
 * @author zhaosong
 * @version 1.0
 * @company 北京海量数据有限公司
 * @date 2024/11/6 22:21
 */
public class TestLock {

    ZooKeeper zk;

    @Before
    public void conn() {
        zk = ZookeeperUtil.getConnection();
    }

    @After
    public void close() throws InterruptedException {
        ZookeeperUtil.close();
    }

    @Test
    public void testlock() {
        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {
                    WatchCallback watchCallBack = new WatchCallback();
                    watchCallBack.setZk(zk);
                    String name = Thread.currentThread().getName();
                    watchCallBack.setThreadName(name);

                    try {
                        //tryLock
                        watchCallBack.tryLock();
                        System.out.println(name + " at work");
                        watchCallBack.getRootData();
//                        Thread.sleep(1000);
                        //unLock
                        watchCallBack.unLock();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
        while (true) {

        }
    }
}
