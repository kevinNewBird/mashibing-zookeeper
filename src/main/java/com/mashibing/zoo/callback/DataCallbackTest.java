package com.mashibing.zoo.callback;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;

/**
 * description: com.mashibing.zoo.callback
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/11/7
 * version: 1.0
 */
public class DataCallbackTest extends BaseTest implements AsyncCallback.DataCallback {

    /**
     * 同步获取节点数据
     * description:
     * create by: zhaosong 2024/11/7 15:41
     */
    @Test
    public void testGetDataBySync() throws InterruptedException, KeeperException, IOException {
        String path = "/teste1";
        // 1.节点数据准备
        zk.create(path, "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 2.获取数据
        Stat stat = new Stat(); // 用于接收状态信息
        byte[] data = zk.getData(path, true, stat); // true是否使用默认的监听器，即创建Zookeeper时传入的DefaultWatcher
        System.out.printf("get data result: %s, %s, %s, %s%n", new String(data), stat.getCzxid(), stat.getMzxid(), stat.getPzxid());

        // 更新数据
        zk.setData(path, "123".getBytes(), -1);// 无视事务id更新数据
    }


    /**
     * 异步获取节点数据
     * description:
     * create by: zhaosong 2024/11/7 15:41
     */
    @Test
    public void testGetDataByAsync() throws InterruptedException, KeeperException {
        String path = "/teste1";
        // 1.节点数据准备
        zk.create(path, "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 2.获取数据
        zk.getData(path, true, this, path); // true是否使用默认的监听器，即创建Zookeeper时传入的DefaultWatcher

        // 3.更新数据
        zk.setData(path, "456".getBytes(), -1);// 无视事务id更新数据
    }

    /**
     * 获取节点数据的异步回调函数
     *
     * @param rc
     * @param path
     * @param ctx
     */
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        if (KeeperException.Code.OK.intValue() != rc) {
            System.err.printf("get data of path[%s] failed:%d%n", path, rc);
            return;
        }
        System.out.printf("get data of the %s, result: %s, %s, %s, %s%n", path
                , new String(data), stat.getCzxid(), stat.getMzxid(), stat.getPzxid());
    }
}
