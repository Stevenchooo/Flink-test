<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<link rel="stylesheet" href="${vars.webRoot}/css/upmob.css">
<link rel="stylesheet" href="${vars.jsRoot}/jquery/jqui/jquery-ui.min.css">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="${vars.jsRoot}/bootstrap-3.3.5-dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${vars.jsRoot}/jquery/jqui/jquery-ui-timepicker-addon.min.css">



<link rel="stylesheet" href="${vars.webRoot}/css/common.css" />
<link rel="stylesheet" href="${vars.webRoot}/css/mms.css">


<title>EMUI 营销管理系统</title>
<script src="${vars.jsRoot}/angular/1.1.5/angular.js"></script>
<script src="${vars.jsRoot}/jquery/jquery/2.1.3/jquery.js"></script>
<script src="${vars.jsRoot}/local/util.js"></script>
<script src="${vars.jsRoot}/local/${curLang}.js"></script>
<script src="${vars.jsRoot}/seajs/2.2.3/sea.js"></script>
<script src="${vars.jsRoot}/modules/base/mms.js"></script>
<script src="${vars.jsRoot}/modules/base/chart.js"></script>
<script src="${vars.jsRoot}/modules/base/meta/meta.js"></script>
<script src="${vars.jsRoot}/echarts/dist/echarts-all.js"></script>
<script src="${vars.jsRoot}/jquery/jqui/jquery-ui.min.js"></script>
<script src="${vars.jsRoot}/jquery/jqui/jquery-ui-timepicker-addon.min.js"></script>
<script src="${vars.jsRoot}/jquery/jquery-cookie-1.4.1/jquery.cookie.js"></script>
<script src="${vars.jsRoot}/modules/base/upmadmin.js"></script>


<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="${vars.jsRoot}/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
<script>
  seajs.config({
    base: "${vars.jsRoot}/",
    alias: {
      'jquery': 'jquery/jquery/2.1.3/jquery',
      'jquery-ui' : 'jquery/jqui/jquery-ui.min',
      'cookie' : 'jquery/jquery-cookie-1.4.1/jquery.cookie',
      'dialog' : 'jquery/dialog/6.0.4/dialog',
      'calendar' : 'jquery/calendar/1.0/tinycal.debug.js',
      'angular' : 'angular/1.1.5/angular',
      'tree' : 'jquery/tree/3.5/ztree',
      'splitter':'jquery/splitter/0.8.0/splitter',
      'meta':'modules/base/meta/meta',
      'navi' :'modules/base/navi',
      'model':'modules/base/model',
      'tools' :'modules/base/tools',
      'notice' :'modules/base/notice',
      'base' :'modules/base/base'
    },
    
    map: [
      [/^(.*\.(?:css|js))(.*)$/i, '$1?20140219']
    ]
  });
var __userKey="${__userKey!""}";
var __cookieHeader="${vars.cookieHead}";
var __webRoot="${vars.webRoot}";
</script>
</head>
<body class="ng-scope">