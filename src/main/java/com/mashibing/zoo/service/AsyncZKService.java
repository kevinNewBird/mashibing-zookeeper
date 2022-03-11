package com.mashibing.zoo.service;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * description  AsyncZKService <BR>
 * <p>
 * author: zhao.song
 * date: created in 11:21  2022/3/11
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class AsyncZKService {

    // 异步创建
    public static void createAsync(ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        // 临时节点
        zooKeeper.create("/javaapiasync", "async".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new AsyncCallback.StringCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                System.out.println(String.format("【method:createAsync】rc:%s====path:%s====ctx:%s====name:%s", rc, path, ctx, name));
            }
        }, null);
    }


    // 异步获取数据
    public static void getDataAsync(ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        // Stat主要是为了获取元数据，即zxid这些信息
        // 注意这个watch是一次性的
        final Stat oStat = new Stat();
        zooKeeper.getData("/javaapiasync", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println(String.format("【method:getDataAsync】rc:%s====path:%s====ctx:%s====data:%s====stat:%s"
                        , rc, path, ctx, new String(data), stat));
            }
        }, "abc");

        // 触发回调：设置数据，返回元数据（这个操作就会触发上面的getData操作的观察watch）
//        final Stat stat = zooKeeper.setData("/javaapisync", "sync new".getBytes(), 0);
        // 会再次触发回调嘛（如果在事件中没有再次设置则不会）
//        final Stat reStat = zooKeeper.setData("/javaapisync", "sync new new".getBytes(), stat.getVersion());
    }

}
