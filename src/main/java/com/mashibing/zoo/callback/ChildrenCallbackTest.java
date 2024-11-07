package com.mashibing.zoo.callback;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.junit.Test;

import java.util.List;

/**
 * description: com.mashibing.zoo.callback
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/11/7
 * version: 1.0
 */
public class ChildrenCallbackTest extends BaseTest implements AsyncCallback.ChildrenCallback {


    /**
     * 同步获取子节点列表（注意：临时节点下不允许创建子节点）
     * description:
     * create by: zhaosong 2024/11/7 17:16
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void testGetChildrenBySync() throws InterruptedException, KeeperException {
        String path = "/c1";
        // 1.创建节点
        zk.create(path, "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create(path + "/x1", "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 2.获取子节点列表
        // 2.1.
        List<String> children = zk.getChildren(path, true);
        System.out.println(children);

        zk.create(path + "/x2", "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        // 2.2.
        children = zk.getChildren(path, true);
        System.out.println(children);

        // 4.删除节点
        zk.delete(path + "/x1", -1);
        zk.delete(path + "/x2", -1);
        zk.delete(path, -1);
    }

    /**
     * 异步获取子节点列表（注意：临时节点下不允许创建子节点）
     * description:
     * create by: zhaosong 2024/11/7 17:16
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    @Test
    public void testGetChildrenByAsync() throws InterruptedException, KeeperException {
        String path = "/c1";
        // 1.创建节点
        zk.create(path, "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create(path + "/x1", "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 2.获取子节点列表
        // 2.1.
        zk.getChildren(path, true, this, null);

        zk.create(path + "/x2", "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        // 2.2.
        zk.getChildren(path, true, this, null);

        // 4.删除节点
        zk.delete(path + "/x1", -1);
        zk.delete(path + "/x2", -1);
        zk.delete(path, -1);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
        // 判断是否更新成功或数据是否存在
        if (KeeperException.Code.OK.intValue() != 0) {
            System.err.printf("get children in the path[%s] failed:%d%n", path, rc);
            return;
        }
        System.out.printf("get children in the %s,  result:%s%n", path, children);
    }
}
