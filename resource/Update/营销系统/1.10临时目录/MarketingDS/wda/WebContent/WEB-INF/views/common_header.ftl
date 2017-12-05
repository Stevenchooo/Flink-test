<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="utf-8" />
<link rel="stylesheet" href="${vars.webRoot}/css/common.css" />
<link rel="stylesheet" href="${vars.webRoot}/css/bootstrap.css" type="text/css" />
<link rel="stylesheet" href="${vars.webRoot}/css/app.css" type="text/css" />
<link rel="stylesheet" href="${vars.webRoot}/css/AdminLTE.css" type="text/css" />

<script src="${vars.jsRoot}/angular/1.1.5/angular.js"></script>
<script src="${vars.jsRoot}/local/util.js"></script>
<script src="${vars.jsRoot}/local/${curLang}.js"></script>
<script src="${vars.jsRoot}/seajs/2.2.1/sea.js"></script>
<script>
  seajs.config({
    base: "${vars.jsRoot}/",
    alias: {
      'jquery': 'jquery/jquery/1.10.1/jquery',
      'cookie' : 'arale/cookie/1.0.2/cookie',
      'dialog' : 'jquery/dialog/4.1.7/artDialog',
      'calendar' : 'jquery/calendar/1.0/tinycal.js',
      'angular' : 'angular/1.1.5/angular',
      'tree' : 'jquery/tree/3.5/ztree',
      'splitter':'jquery/splitter/0.8.0/splitter',
      'meta':'modules/base/meta/meta',
      'model':'modules/base/model'
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
<body>