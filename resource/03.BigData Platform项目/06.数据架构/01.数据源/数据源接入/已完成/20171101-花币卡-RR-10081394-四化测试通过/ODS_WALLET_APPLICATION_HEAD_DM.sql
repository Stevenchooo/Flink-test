
ALTER TABLE ODS_WALLET_APPLICATION_HEAD_DM ADD columns(cardType string comment '申卡类型');
ALTER TABLE ODS_WALLET_APPLICATION_HEAD_DM ADD columns(revenueDeptName string comment '受益部门名称');
ALTER TABLE ODS_WALLET_APPLICATION_HEAD_DM ADD columns(revenueDeptNo string comment '受益部门编码');
ALTER TABLE ODS_WALLET_APPLICATION_HEAD_DM ADD columns(revenueProductName string comment '受益产品名称');
ALTER TABLE ODS_WALLET_APPLICATION_HEAD_DM ADD columns(revenueProductNo string comment '受益产品编码');
ALTER TABLE ODS_WALLET_APPLICATION_HEAD_DM ADD columns(cardShape string comment '卡形态');
ALTER TABLE ODS_WALLET_APPLICATION_HEAD_DM ADD columns(grantChannel string comment '发放渠道');
ALTER TABLE ODS_WALLET_APPLICATION_HEAD_DM ADD columns(country string comment '发行国家');
ALTER TABLE ODS_WALLET_APPLICATION_HEAD_DM ADD columns(currency string comment '发行货币');
