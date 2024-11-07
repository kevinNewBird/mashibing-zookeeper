package com.mashibing.zoo.callback;

import com.mashibing.zoo.util.ZookeeperUtil;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;

/**
 * description: com.mashibing.zoo.callback
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/11/7
 * version: 1.0
 */
public abstract class BaseTest {
    protected ZooKeeper zk;

    @Before
    public void prepare() {
        zk = ZookeeperUtil.getConnection();
    }

    @After
    public void destroy() throws InterruptedException {
        zk.close();
    }
}
