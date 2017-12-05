<div class="upmc-wp" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
  
  <div class="upmob-filter">
    <table style="width:100%">
        <tr>
        <td style="LINE-HEIGHT: 24px;">${lang.searchCriteria}
            <input type="text" ng-trim="true" maxlength="32" ng-keypress="pageClick(0)" ng-model="queryName" style="width:200px;height:24px;">
        </td>
        <td align="right">
            <input type="button" ng-click="pageClick(0)"
            style="width: 80px;" class="btn-lightblue" value="${lang.query}">
        </td>
        </tr>
    </table>    
    <br/>
    <div class="upmc-controls upmcc-multi"> 
    	<a href="javascript:void(0)" ng-click="modelCreate()"><b class="link-icon li-batinput"></b> <span class="vm">${lang.dicMediaWebCreate}</span></a>       
    </div>
    <div class="up-detable">  
      <table>
        <tr>
             <th>${lang.dicMediaName}</th>
             <th>${lang.dicWebName}</th>
             <th>${lang.belong}</th>
             <th>${lang.adInfoOper}</th>
        </tr>
        <tr ng-repeat="row in resp.results">
             <td>{{row.pName}}</td>
             <td>{{row.name}}</td>
             <td>{{row.type}}</td>
             <td>
                 <a href="javascript:void(0)" ng-click="modelModify('{{row.pid}}' + '|' + '{{row.id}}' +  '|' + '{{row.name}}')" class="tb-edit"  ng-show="row.flag">${lang.dicWebModify}</a>
                 <a href="javascript:void(0)" ng-click="modelDel('{{row.pid}}' + '|' + '{{row.id}}')" class="tb-edit"  ng-show="row.flag">${lang.dicWebDel}</a>
             </td>
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
    
    
<div id="id_modify_mktDicInfo_dialog" style="display:none;font-size: 12px;">
  <form name="mktDicInfoModifyForm" class="css-form" novalidate autocomplete="off">
   <table border="0" class="css-table">

   <tr>
    <td><input  type="radio" name="checkWebName" ng-model="checkWebName" value="media" style="width:20px;" required/>${lang.dicMediaName}:&nbsp;</td>
    <td >
     <select ng-show="isQueryMktDicInfoName()" ng-model="queryMktDicInfoName" 
    	 ng-trim="true" ng-options="p.id as p.name for p in medias" style="height:24px;width:320px">
 	 </select>
 	 <span ng-show="isMktDicInfoName()">
       <input  type="text" name="mktDicInfoName" ng-model="mktDicInfoName" style="height:24px;width:320px" ng-trim="true" ng-minlength="1"  ng-maxlength="100" required/>
       <span class="error">*&nbsp;</span>
       <span class="error" ng-show="mktDicInfoModifyForm.mktDicInfoName.$invalid">${lang.adInfoMediaTypeValue_validate}</span>
     </span>
     <br>
    </td>
   </tr>
   <tr >
    <td><input  type="radio" name="checkWebName" ng-model="checkWebName" value="web" style="width:20px;"  required/>${lang.dicWebName}:&nbsp;</td>
    <td style="">
     <input type="text" name="dicWebNameCreate" ng-model="dicWebNameCreate" ng-disabled="isMktDicInfoName()" style="height:24px;width:320px" ng-trim="true" ng-minlength="1" ng-maxlength="100"  required/>
     <span ng-show="isQueryMktDicInfoName()">
       <span class="error">*&nbsp;</span>
       <span class="error" ng-show="mktDicInfoModifyForm.dicWebNameCreate.$invalid">${lang.adInfoWebNameValue_validate}</span><br>
     </span>
    </td>
   </tr>
   
  </table>
  </form>
  </div>  
  
    </div> <!-- end of up-detable -->  
</div> <!-- end of upmob-filter -->     
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->



<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/mktMediaDic"], function(model, MetaMktMediaDic) {
    var meta =  MetaMktMediaDic.instance("${vars.webRoot}", 'mktMediaDic', ["mktMediaDic","name"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:15
    });
});
</script>