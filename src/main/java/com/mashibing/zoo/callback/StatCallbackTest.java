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
public class StatCallbackTest extends BaseTest implements AsyncCallback.StatCallback {

    /**
     * 同步更新节点数据
     * description:
     * create by: zhaosong 2024/11/7 15:41
     */
    @Test
    public void testSetDataBySync() throws InterruptedException, KeeperException, IOException {
        String path = "/teste1";
        // 1.节点数据准备
        zk.create(path, "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 2.更新数据
        zk.setData(path, "123".getBytes(), -1);// 无视事务id更新数据
    }


    /**
     * 异步更新节点数据
     * description:
     * create by: zhaosong 2024/11/7 15:41
     */
    @Test
    public void testSetDataByAsync() throws InterruptedException, KeeperException, IOException {
        String path = "/teste1";
        // 1.节点数据准备
        zk.create(path, "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 2.更新数据
        zk.setData(path, "456".getBytes(), -1, this, null);// 无视事务id更新数据

        System.in.read();
    }

    /**
     * 同步检查节点是否存在
     * description:
     * create by: zhaosong 2024/11/7 15:41
     */
    @Test
    public void testExistsBySync() throws InterruptedException, KeeperException, IOException {
        String path = "/teste1";
        // 1.检查节点是否存在(还没创建的节点，仍能监听)
        Stat stat = zk.exists(path, true);
        // 2.创建节点
        zk.create(path, "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }


    /**
     * 异步检查节点是否存在(不建议使用)
     * description:
     * create by: zhaosong 2024/11/7 15:41
     */
    @Test
    public void testExistsByAsync() throws InterruptedException, KeeperException, IOException {
        String path = "/teste1";
        // 1.检查节点是否存在(还没创建的节点，仍能监听)
        zk.exists(path, true, this, null);

        // 2.创建节点
        zk.create(path, "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.in.read();
    }


    /**
     * 异步更新节点数据或检查节点是否存在的回调接口
     *
     * @param rc：调用的返回码
     * @param path:异步调用时的路径参数
     * @param ctx：异步调用时的上下文对象
     * @param stat：用于接收状态信息
     */
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        // 判断是否更新成功或数据是否存在
        if (KeeperException.Code.OK.intValue() != 0) {
            System.err.printf("update data or exists in the path[%s] failed:%d%n", path, rc);
            return;
        }
        System.out.printf("update data or exists in the %s,  result: %s, %s, %s%n"
                , path, stat.getCzxid(), stat.getMzxid(), stat.getPzxid());
    }
}
