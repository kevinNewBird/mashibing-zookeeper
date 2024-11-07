package com.mashibing.zoo.callback;

import com.mashibing.zoo.util.ZookeeperUtil;
import org.apache.zookeeper.*;
import org.junit.Test;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * description: com.mashibing.zoo.callback
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/11/7
 * version: 1.0
 */
public class StringCallbackTest extends BaseTest implements AsyncCallback.StringCallback {

    /**
     * 同步创建节点
     * description:
     * create by: zhaosong 2024/11/7 13:54
     */
    @Test
    public void testCreateBySync() throws InterruptedException, KeeperException {
        zk = ZookeeperUtil.getConnection();
        // 1.持久节点
        zk.create("/testcs1", "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        // 2.持久序列节点
        zk.create("/testcs2", "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);

        // 3.临时节点
        zk.create("/testcs3", "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        // 4.临时序列节点
        zk.create("/testcs4", "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

    }

    /**
     * 异步创建节点
     * description:
     * create by: zhaosong 2024/11/7 13:54
     */
    @Test
    public void testCreateByAsync() throws InterruptedException, KeeperException, IOException {
        ZooKeeper zk = null;
        try {
            zk = ZookeeperUtil.getConnection();
            // 1.持久节点
            zk.create("/testcs1", "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE
                    , CreateMode.PERSISTENT, this, "persistent");
            // 2.持久序列节点
            zk.create("/testcs2", "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE
                    , CreateMode.PERSISTENT_SEQUENTIAL, this, "persistent_seq");

            // 3.临时节点(注意：临时节点会话关闭不是立即删除的，所以需要最好正常退出的情况下对其进行手动删除)
            zk.create("/testcs3", "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE
                    , CreateMode.EPHEMERAL, this, "ephemeral");

            // 4.临时序列节点
            zk.create("/testcs4", "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE
                    , CreateMode.EPHEMERAL_SEQUENTIAL, this, "ephemeral_seq");

            TimeUnit.SECONDS.sleep(5);
            System.in.read();
        } finally {
            if (null != zk) {
                zk.close();
            }
        }

    }


    /**
     * 异步创建节点回调接口
     *
     * @param rc：调用的返回码
     * @param path:异步调用时的路径参数
     * @param ctx：异步调用时的上下文对象
     * @param name：实际创建的节点名。成功时通常同path相同，除非创建的是sequential节点 (name-注意：对于临时节点，name可能会有空的情况，使用的版本3.4.6；临时序列节点未发现该问题)
     */
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        // 判断是否创建成功
        if (KeeperException.Code.OK.intValue() != 0) {
            System.err.printf("create path[%s] failed:%d%n", path, rc);
            return;
        }
        // 用于验证name可能为空的情况(bug?)
//        String s = name.substring(1);
        // 注意：连接关闭，临时节点不会立即删除。为确保可靠，除异常情况外，临时节点需采用手动删除的方式（比如分布式锁）
        System.out.printf("create path result: [%d] , %s , %s, %s%n", rc, path, ctx, name);

    }
}
