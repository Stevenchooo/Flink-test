һ.����hive��hbase��
     1.��root�û���¼hadoop����������������Ŀ¼upgrade
     2.�ϴ�Upgrade.sh�ļ���upgradeĿ¼��
     3.ִ���������
	   chmod 755 Upgrade.sh
           sh Upgrade.sh
     4.�۲�upgradeĿ¼��Upgrade.log�Ƿ��д�����־��
 
��.����DM����ӿ��ļ�����������Ҫ������
   1.ͨ��sftp���񱸷�$HOME/dm_home/config�µ�hbase_edata_edp_video_user_active.xml��hbase_edata_edp_video_user_retention.xml��hbase_edata_edp_video_user_develop_appversion_d.xml������
   2.ͨ��sftp�����滻DM_Config/���ļ���$HOME/dm_home/config�µ�ͬ���ļ���
   3.��edatadm�û���¼dm������
   4.����dm
     wt
     ws


��.������������ļ�(index��report�ļ�����������Ҫ����)
   1.ͨ��sftp���񱸷�$HOME/report_home/config/report�µ������ļ������ء�
   2.�ֱ���BDI_Report�µ�index��Report�ļ��滻$HOME/report_home/config/reportͬ����index��Report�ļ���


��.����portal�ļ�(��������Ҫ����)
   1.ͨ��sftp���񱸷�portal��������$HOME/eportal_home/webapps/edp/eData/video�µ�dashboard-user-retention.html��dashboard-user-active.html��dashboard-cp-settlement-                 statistics-svod.html�ļ� 
   2.ͨ��sftp���񱸷�portal��������$HOME/eportal_home/webapps/edp/bundles/i18n�µ�Messages_Video_en.properties��Messages_Video_zh.properties�ļ�
   3.ͨ��sftp���񱸷�portal��������$HOME/eportal_home/webapps/edp/data�µ�channel-data_video.js�ļ�
   4.ͨ��sftp�����滻Portal/���ļ���$HOME/eportal_home/webapps/edp/eData/video�µ�ͬ���ļ���
   5.ͨ��sftp�����滻Portal/���ļ���$HOME/eportal_home/webapps/edp/bundles/i18n�µ�ͬ���ļ���
   6.ͨ��sftp�����滻Portal/���ļ���$HOME/eportal_home/webapps/edp/data�µ�ͬ���ļ���
   7.����portal���棬���platformid�Ƿ�����չʾ���ݡ�
   
��.�޸�bdi������
     1���һ� video ,ѡ�������̣�ѡ��BDI_Config/���ļ�������ģʽѡ��"�������£������µ��ȣ�",�����û��������룬���ȷ��
     2������ɹ����һ���������ѡ�� ����
     3�����ε�������������
    

��ע��	 
   �����Ҫ���˵�֮ǰ�汾����ִ�����������
        1.��root�û���¼hadoop������
	2.�ϴ�RollBack.sh��upgradeĿ¼
	3.ִ�����������Ĭ�ϻָ�ʱ���201705��ʼ
           chmode 755 RollBack.sh
	   sh RollBack.sh
	4.���upgrade����־�Ƿ�����
        5.��ԭ���������ģ��岽�ļ���