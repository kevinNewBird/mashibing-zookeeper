package com.mashibing.zoo.service;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

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
    public static void createSync(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {
        String result = zooKeeper.create(path, "sync".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(result);
    }

    // 同步创建
    public static void createSyncAclByIp(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {
        List<ACL> acls = new ArrayList<>();
        Id id = new Id("ip", "192.168.31.185");
        ACL acl = new ACL(ZooDefs.Perms.ALL, id);
        acls.add(acl);

        Id id2 = new Id("ip", "192.168.231.150");
        ACL acl2 = new ACL(ZooDefs.Perms.ALL, id2);
        acls.add(acl2);

        Id id3 = new Id("ip", "127.0.0.1");
        ACL acl3 = new ACL(ZooDefs.Perms.ALL, id3);
        acls.add(acl3);
        String result = zooKeeper.create(path, "sync".getBytes(), acls, CreateMode.EPHEMERAL);
        System.out.println(result);
    }

    // 同步创建带有权限控制
    public static void createSyncAcl(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {
        List<ACL> acls = new ArrayList<>();
        // admin后面的跟着的必须为base64加密密码，由 user:password 组合后生成
        // echo -n admin:admin-secret | openssl dgst -binary -sha1 | openssl base64
        Id id = new Id("digest", "admin:a6v9bDs6OoZr04/JO8wCjCHWzMs=");
        ACL acl = new ACL(ZooDefs.Perms.ALL, id);
        acls.add(acl);

        String result = zooKeeper.create(path, "test".getBytes(), acls, CreateMode.EPHEMERAL);
        System.out.println(result);
    }

    // 同步阻塞获取数据
    public static void getDataSync(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {
        // Stat主要是为了获取元数据，即zxid这些信息
        // 注意这个watch是一次性的
        final Stat oStat = new Stat();
        byte[] data = zooKeeper.getData(path, new Watcher() {
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
                    zooKeeper.getData(path, this, oStat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, oStat);

        System.out.printf("【method:getDataSync】path:%s,data:%s%n", path, new String(data));
        // 触发回调：设置数据，返回元数据（这个操作就会触发上面的getData操作的观察watch）
        final Stat stat = zooKeeper.setData(path, "sync new".getBytes(), 0);
        // 会再次触发回调嘛（如果在事件中没有再次设置则不会）
        final Stat reStat = zooKeeper.setData(path, "sync new new".getBytes(), stat.getVersion());
        final Stat reStat2 = zooKeeper.setData(path, "sync new new new".getBytes(), reStat.getVersion());
    }

    public static void deleteSync(ZooKeeper zooKeeper, String path) throws InterruptedException, KeeperException {
        zooKeeper.delete(path,-1);
    }
}
