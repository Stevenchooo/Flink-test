��ṹ�޸ģ�
       �漰��ODS_DEV_ADV_T_TASK_DS��ODS_DEV_ADV_CONTENT_HM��ODS_DEV_ADV_SLOT_HM
       ������£�
alter table ODS_DEV_ADV_T_TASK_DS  add columns (alliance_app_id      STRING COMMENT '���ƹ�Ӧ�û�Ϊ���������˵�Ӧ��ID������10135247');
alter table ODS_DEV_ADV_CONTENT_HM add columns (downloadAll          STRING COMMENT '��Ӧ�����������');
alter table ODS_DEV_ADV_CONTENT_HM add columns (downloadEffective    STRING COMMENT '��ЧӦ����������� ');
alter table ODS_DEV_ADV_CONTENT_HM add columns (virtualTaskWinAll    STRING COMMENT '��������ʤ��������');
alter table ODS_DEV_ADV_SLOT_HM    add columns (downloadAll          STRING COMMENT '��Ӧ�����������');
alter table ODS_DEV_ADV_SLOT_HM    add columns (downloadEffective    STRING COMMENT '��ЧӦ����������� ');   