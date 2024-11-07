# 介绍

zk中针对异步获创建、删除、查询数据等，提供了各种回调callback方法。下面是几种callback：<br/>

- StringCallback： 创建节点create
- VoidCallback： 删除节点delete
- StatCallback： 更新节点数据setData或检查节点是否存在exists
- DataCallback: 获取节点数据getData
- ChildrenCallback：获取子节点列表getChildren的回调
- Children2Callback：获取子节点列表getChildren的回调（和ChildrenCallback的区别在于接收参数的不同）