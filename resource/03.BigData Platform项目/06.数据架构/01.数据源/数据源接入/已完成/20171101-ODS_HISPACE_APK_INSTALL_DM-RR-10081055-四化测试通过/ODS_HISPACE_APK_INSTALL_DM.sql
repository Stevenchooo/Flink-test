alter table ODS_HISPACE_APK_INSTALL_DM add columns (emuiVer string comment 'emui版本');
alter table ODS_HISPACE_APK_INSTALL_DM add columns (phoneType string comment '机型');
alter table ODS_HISPACE_APK_INSTALL_DM add columns (callerPackage string comment '调用安装器应用的包名');
 