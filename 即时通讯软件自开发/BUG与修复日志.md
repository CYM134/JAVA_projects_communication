## JAVA大作业：通信软件开发日志



10月25日-11月11日：前期准备工作，包括网络编程、多线程、IO、`JAVAFX`等基础知识的学习，在`GitHub`、`CSDN`、B站、知乎、贴吧、油管等`APP`中完成(资料收集、知识学习和基础框架参考)  By:崔

------

11月12日：Client端基础common软件包、service软件包框架搭建。  By：崔

11月13日：Server端基础common软件包、service软件包搭建。  By：崔

------

11月15日：Server端服务端口搭建、实现管理和客户端通信的线程、覆写Thread中的run方法确保长时间即时通信。Client实现覆写Thread中的run方法，保持服务端与客户端的长时间联通、实现管理客户端连接到服务端的线程的类。  By：崔

1、BUG描述：不能长时间保存通讯，在一两分钟的连通后会断开.

修复：修改run方法为一旦持有socket就会长时间循环监听，直到系统退出才中止进程。  By:崔

------

11月16日：客户端`UI`登录界面的实现、实现一级界面的登录功能和退出功能，完善服务端逻辑处理部分 。

By:崔

1、增加了注册功能，确保可以注册新用户。 By：崔

2、`HashMap`用来存储用户数据，面临的问题是在多线程时是线程不安全的，而`ConcurrentHashMap`则是线程同步处理，在多线程下是安全的,因此采用`ConcurrentHashMap`，弃用原来保存数据的HashMap。By：崔

3、BUG描述：`ConcurrentHashMap`不能够实现实时更新，而且在单次运行服务端时保存的用户，在下一次运行会丢失。

修复：在运行代码时，任何对用户数据的修改都是临时的，所以弃用对集合的使用，改为使用文件存储的方式，修改为从文件加载用户数据，来保证用户数据的长期有效性，保证每次关闭服务端后数据不会丢失。By：崔

4、BUG描述：每次重新联网后，服务端与客户端之间会失去连接。

操作：手动修改`IP`地址即可。  By：崔

------

11月19日：完成客户端二级`UI`界面的搭建，实现显示在线用户列表功能。 By:崔

BUG描述：键入二级菜单时，一级菜单不会消失。

修复：改为方法调用二级菜单，传入一级菜单的stage作为参数，实现更新式键入二级菜单。 By：崔

BUG描述：服务端与客户端之间，只要任何一方提前停止运行，另一方会出现无限循环读取IO的错误。

修复：归根结底是因为多个进程运行导致的。尝试实现无异常退出功能修复BUG。 By：崔

------

11月22日：实现系统无异常退出功能。By:崔

BUG：有时还会出现IO读取异常，但只循环几次就停止了，不会无限读取下去，原因未知，修复不了。部分时候能正常实现无异常退出。应该是进程、main线程、多个和服务端通信的线程之间谁先终止运行的先后问题。没实力，修不了。

BY:崔

新BUG：只有点击按钮才能实现无异常退出功能，点击右上角的叉叉退出还是会陷入循环。

修复：右上角的关闭窗口键可以设置`setOnCloseRequest`来设置点击后的事件，添加代码进去就可以了。

By：崔

------

11月25日-11月26日：完成了私聊功能。By：崔

1、BUG:不能对文件中的任意用户发起聊天。

描述：因为没有使用数据库，所以我们只能对持有socket的客户端发起私聊，就是在线的用户才能私聊，不然肯定会报错。增加提示界面。 By：崔

2、BUG描述：显示消息的文本框可以编辑，而且不知道是谁和谁聊天。

修复：改文本框为`AreaText`，且禁用其文本编辑功能，增加时间戳,改为从文本框中读取消息，然后再传送，再清空文本框，同时该为按下`enter`键可以发送消息。BY:崔

3、接收消息的用户不能够显示消息，发了看不见。

修复：在run方法中增加`MESSAGE_COMM_MES_SEND`消息类型的判断，接收到这个消息类型的客户端就是收到消息的用户，设置弹出聊天窗口显示收到的消息。By：崔

4、多次发送消息时，每次发送消息收到消息的一方都会弹出新窗口显示。

修复：使用Hash Map来存储stage消息窗口，如果检查到该用户有窗口的话，就在既有窗口中显示消息，直接聚焦，没有窗口再创建并放入集合中。  BY：崔

5、收到消息的用户在使用弹出聊天窗口回复消息时，发出信息的人会弹出新窗口而不用既有窗口。

修复：应该是message的改变致使键发生改变导致的，原来的发生者变成了接收者，但是在原来的线程里只有发送者的stage……，修改键标识为："发送方-接收方" 的组合来作为键，这样可以唯一标识聊天窗口了。By：崔

6、私聊窗口只能出现一次，再次使用私聊功能时，窗口不能再显示了。

修复：忘记在方法里添加点击关闭按钮就移除出集合的代码了…………  By：崔

------

12月4日-12月6日：完成了私聊界面发送文件的功能。By：崔

1、BUG描述：使用`FileInputStream`流不能正确读取文件。

解决：在`MessageType`中增加字节数组，采用二进制形式传输文件，再取出message中的文件字节数组，并通过文件输出流写出到磁盘上。  By：崔

BUG描述：发送文件时`DirectoryChooser`得到了保存的目录，但是文件不能正常保存。

解决：通过`DirectoryChooser`得到的只是保存的目录，没有包括文件名，可以通过`String`中的`lastIndexOf`方法得到最后的文件名设置为默认文件名，再通过字符串拼接得到完整的路径，使之变成合法的文件保存格式。   By：崔

BUG描述：发送文件时，发送方可以正常发送，但是接收方不能发送文件回去给发送方。

解决：逻辑问题。在接收者的界面还是sender和getter的顺序，应该是getter和sender的顺序才对，毕竟接收者是getter，在接收者的界面还是让发送者来发，那肯定跑不了  By：崔

------

12月12日：完成了群聊功能(只能发送消息)采用了思维导图方式。   By:崔

![](C:/Users/30548/Pictures/联想截图/聊天室的实现思路.png)
