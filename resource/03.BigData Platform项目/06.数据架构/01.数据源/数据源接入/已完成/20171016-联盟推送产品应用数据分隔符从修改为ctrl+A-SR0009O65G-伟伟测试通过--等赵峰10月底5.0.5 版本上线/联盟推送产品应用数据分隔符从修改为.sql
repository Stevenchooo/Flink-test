ODS_DEV_GLOBAL_PRODUCT_UP_DM
ODS_DEV_GLOBAL_SERVICE_UP_DM
ODS_DEV_GLOBAL_APP_APK_DM
ODS_DEV_GLOBAL_APP_LANGUAGE_DM
ODS_DEV_GLOBAL_APP_DM

alter table ODS_DEV_GLOBAL_PRODUCT_UP_DM set SERDEPROPERTIES (
  'field.delim'='\u0001',
  'line.delim'='\n',
  'serialization.format'='\u0001')
  
  alter table ODS_DEV_GLOBAL_SERVICE_UP_DM set SERDEPROPERTIES (
  'field.delim'='\u0001',
  'line.delim'='\n',
  'serialization.format'='\u0001')

  alter table ODS_DEV_GLOBAL_APP_APK_DM set SERDEPROPERTIES (
  'field.delim'='\u0001',
  'line.delim'='\n',
  'serialization.format'='\u0001')
  
  alter table ODS_DEV_GLOBAL_APP_LANGUAGE_DM set SERDEPROPERTIES (
  'field.delim'='\u0001',
  'line.delim'='\n',
  'serialization.format'='\u0001')
  
  alter table ODS_DEV_GLOBAL_APP_DM set SERDEPROPERTIES (
  'field.delim'='\u0001',
  'line.delim'='\n',
  'serialization.format'='\u0001')