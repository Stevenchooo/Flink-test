表结构修改：
       涉及表：ODS_DEV_ADV_T_TASK_DS、ODS_DEV_ADV_CONTENT_HM、ODS_DEV_ADV_SLOT_HM
       语句如下：
alter table ODS_DEV_ADV_T_TASK_DS  add columns (alliance_app_id      STRING COMMENT '被推广应用华为开发者联盟的应用ID，例如10135247');
alter table ODS_DEV_ADV_CONTENT_HM add columns (downloadAll          STRING COMMENT '总应用下载完成数');
alter table ODS_DEV_ADV_CONTENT_HM add columns (downloadEffective    STRING COMMENT '有效应用下载完成数 ');
alter table ODS_DEV_ADV_CONTENT_HM add columns (virtualTaskWinAll    STRING COMMENT '虚拟任务胜出次数的');
alter table ODS_DEV_ADV_SLOT_HM    add columns (downloadAll          STRING COMMENT '总应用下载完成数');
alter table ODS_DEV_ADV_SLOT_HM    add columns (downloadEffective    STRING COMMENT '有效应用下载完成数 ');   