<div class="upmc-wp" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
  <div class="upmob-filter">
  
  <table style="width:100%">
        <tr>
        	
        <td style="LINE-HEIGHT: 24px;padding-right:70px;">${lang.pOperator}
            <input type="text" ng-trim="true" maxlength="32" ng-keypress="" ng-model="queryName" style="height:24px;width:160px">
        </td>
        <td style="LINE-HEIGHT: 24px;padding-right:70px;">${lang.pOperatDate}
            <input style="width:160px;height:24px;width:160px" type="text" readonly="true" id="queryDate" name="queryDate" ng-model="queryDate" required/>
        </td>
        <td >
            <input type="button" ng-click="logExport()"
            style="width: 80px;" class="btn-lightblue" value="${lang.reportExport}">
        </td>
        
        <td align="right">
            <input type="button" ng-click="pageClick(0)"
            style="width: 80px;" class="btn-lightblue" value="${lang.query}">
        </td>
        </tr>
    </table>
      
    <br/>
    <div class="upmc-controls upmcc-multi">        
    </div>
    <div class="up-detable">	    
      <table>
        <tr>
             <th width="15%">${lang.pOperatTime}</th>
             <th width="15%">${lang.pOperator}</th>
             <th width="10%">${lang.pOperatReCode}</th>
             <th width="15%">${lang.pOperatRequest}</th>
             <th width="45%">${lang.pOperatResponse}</th>
        </tr>
        <tr ng-repeat="row in resp.result">
             <td>{{row.operTime}}</td>
             <td>{{row.operator}}</td>
             <td>{{row.operRes}}</td>
             <td>{{row.opeRequest}}</td>
             <td>{{row.opeRrsponse}}</td>
        </tr>
    </table>
    
    <div class="up-pager">
        <div class="uppg">
            <a href="javascript:void(0);" ng-repeat="pg in pages" ng-click="pageClick({{pg.from}})" class="{{pg.cls}}">{{pg.str}}</a>
        </div>
        <div class="pg-count">
                     共<span class="pg-no">{{pageNum}}</span>页
        </div>
        
    </div>
    <div id="goto_page_window" style="width:${lang.pageWindowWidth}px;">
    <table border="0" style="margin:1px;"><tr>
      <td><input type="text" ng-model="gotoPageNo" class="page_input"></td>
      <td ng-click="pageClick(-2)" style="cursor:pointer;color:blue;">${lang.goToPage}</td></tr>
    </table>
    </div>
    
    
  
    </div> <!-- end of up-detable -->  
</div> <!-- end of upmob-filter -->    
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->



<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/mktOperLog"], function(model, MetaMktOperLog) {
    var meta =  MetaMktOperLog.instance("${vars.webRoot}", 'mktOperLog', ["mktOperLog","name"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:15
    });
});
</script>