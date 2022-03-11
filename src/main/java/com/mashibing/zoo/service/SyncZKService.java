package com.mashibing.zoo.service;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * description  SyncZKService <BR>
 * <p>
 * author: zhao.song
 * date: created in 11:20  2022/3/11
 * company: TRS信息技术有限公司
 * version 1.0
 */
public class SyncZKService {

    // 同步创建
    public static void createSync(ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        String result = zooKeeper.create("/javaapisync", "sync".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(result);
    }

    // 同步阻塞获取数据
    public static void getDataSync(ZooKeeper zooKeeper) throws InterruptedException, KeeperException {
        // Stat主要是为了获取元数据，即zxid这些信息
        // 注意这个watch是一次性的
        final Stat oStat = new Stat();
        byte[] data = zooKeeper.getData("/javaapisync", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("【method:getDataSync】" + event.toString());
                // 在事件中，再次注册，确保每次读取操作都能被监听且触发回调
                // true表示defualt watch被注册，也就是new zk时的watch
                // 在这里我们应该使用this对象
//                Try.run(() -> zooKeeper.getData("/javaapisync", true, oStat))
//                        .onFailure(ex -> ex.printStackTrace());
//                Try.run(() -> zooKeeper.getData("/javaapisync", this, oStat))
//                        .onFailure(ex -> ex.printStackTrace());
                try {
                    zooKeeper.getData("/javaapisync", this, oStat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, oStat);

        System.out.println("【method:getDataSync】" + new String(data));
        // 触发回调：设置数据，返回元数据（这个操作就会触发上面的getData操作的观察watch）
        final Stat stat = zooKeeper.setData("/javaapisync", "sync new".getBytes(), 0);
        // 会再次触发回调嘛（如果在事件中没有再次设置则不会）
        final Stat reStat = zooKeeper.setData("/javaapisync", "sync new new".getBytes(), stat.getVersion());
        final Stat reStat2 = zooKeeper.setData("/javaapisync", "sync new new new".getBytes(), reStat.getVersion());
    }
}
