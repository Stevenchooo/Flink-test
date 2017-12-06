一.升级hive和hbase表
     1.以root用户登录hadoop服务器，创建升级目录upgrade
	 2.上传Upgrade.sh文件到upgrade目录。
	 3.执行下面命令。
	   chmod 755 Upgrade.sh
       sh Upgrade.sh
	 4.观察upgrade目录下Upgrade.log是否有错误日志。
 
二.升级DM报表接口文件（主备机都要操作）
   1.通过sftp服务备份$HOME/dm_home/config下的hbase_edata_edp_video_user_access.xml和hbase_edata_edp_video_user_active.xml到本地
   2.通过sftp服务，替换DM_Config/的文件到$HOME/dm_home/config下的同样文件。
   3.以edatadm用户登录dm服务器
   4.重启dm
     wt
	 ws


三.升级报表结算文件(index和report文件，主备机都要操作)
   1.通过sftp服务备份$HOME/report_home/config/report下的所有文件到本地。
   2.分别用BDI_Report下的index和Report文件替换$HOME/report_home/config/report同样的index和Report文件。


四.升级portal文件(主备机都要操作)
   1.通过sftp服务备份portal服务器的$HOME/eportal_home/webapps/edp/eData/video下的dashboard-user-access.html和dashboard-user-active.html文件
   2.通过sftp服务，替换Portal/的文件到$HOME/eportal_home/webapps/edp/eData/video下的同样文件。
   3.访问portal界面，检查platformid是否正常展示数据。
   
五.修改bdi控制流
     1、右击 video ,选择导入流程，选择BDI_Config/下文件，导入模式选择"插入或更新（不更新调度）",输入用户名、密码，点击确定
     2、导入成功后，右击控制流，选择 发布
     3、依次导入三个控制流
     4、右击 基础数据结算 控制流，选择设置调度时间，调度时间设置为每天11:30，优先级设置3,节点选择 192.168.15.147，不勾选禁用调用时间，设置完成后点击确定    



备注：	 
   如果需要恢复之前数据，需执行下面操作：
    1.以bdi用户登录bdi服务器，创建临时目录upgrade
	2.上传Recover.sh到upgrade目录
	3.执行下面操作，默认恢复时间从201705开始
       chmode 755 Recover.sh
	   sh Recover.sh
	4.检查upgrade下日志是否正常