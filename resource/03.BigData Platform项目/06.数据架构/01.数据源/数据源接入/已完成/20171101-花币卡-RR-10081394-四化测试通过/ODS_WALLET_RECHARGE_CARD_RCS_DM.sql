
ALTER TABLE ODS_WALLET_RECHARGE_CARD_RCS_DM ADD COLUMNS (cardType string comment '申卡类型');
ALTER TABLE ODS_WALLET_RECHARGE_CARD_RCS_DM ADD COLUMNS (cardShape string comment '卡形态');
ALTER TABLE ODS_WALLET_RECHARGE_CARD_RCS_DM ADD COLUMNS (grantChannel string comment '发放渠道');
ALTER TABLE ODS_WALLET_RECHARGE_CARD_RCS_DM ADD COLUMNS (country string comment '发行国家');
ALTER TABLE ODS_WALLET_RECHARGE_CARD_RCS_DM ADD COLUMNS (currency string comment '发行货币');
ALTER TABLE ODS_WALLET_RECHARGE_CARD_RCS_DM ADD COLUMNS (grantStatus string comment '发放状态');
