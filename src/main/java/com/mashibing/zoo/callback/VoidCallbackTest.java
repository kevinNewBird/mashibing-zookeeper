package com.mashibing.zoo.callback;

import com.mashibing.zoo.util.ZookeeperUtil;
import org.apache.zookeeper.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * description: com.mashibing.zoo.callback
 * company: 北京海量数据有限公司
 * create by: zhaosong 2024/11/7
 * version: 1.0
 */
public class VoidCallbackTest extends BaseTest implements AsyncCallback.VoidCallback {

    /**
     * description:同步删除节点
     * create by: zhaosong 2024/11/7 14:46
     */
    @Test
    public void testDeleteBySync() throws InterruptedException, KeeperException, IOException {
        // 1.节点数据准备
        zk.create("/teste1", "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 2.删除节点(不设置默认监听器)
        zk.delete("/teste1", -1);

        System.in.read();
    }

    /**
     * description:异步删除节点
     * create by: zhaosong 2024/11/7 14:46
     */
    @Test
    public void testDeleteByAsync() throws InterruptedException, KeeperException, IOException {
        // 1.节点数据准备
        zk.create("/teste1", "create data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        // 2.删除节点
        zk.delete("/teste1", -1, this, "async");

        System.in.read();
    }


    /**
     * 删除节点的异步回调函数
     *
     * @param rc
     * @param path
     * @param ctx
     */
    @Override
    public void processResult(int rc, String path, Object ctx) {
        if (KeeperException.Code.OK.intValue() != rc) {
            System.err.printf("delete path[%s] failed:%d%n", path, rc);
            return;
        }
        System.out.printf("delete path result: %d, %s, %s%n", rc, path, ctx);
    }
}
