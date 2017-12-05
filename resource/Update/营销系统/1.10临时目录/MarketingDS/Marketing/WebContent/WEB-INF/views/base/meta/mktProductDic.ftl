<div class="upmc-wp"  ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
  
  <div class="upmob-filter">
    <br/>
    <p>${lang.mktProductDic}</p>
    <br/>
    <div class="upmc-controls upmcc-multi"> 
    	<a href="javascript:void(0)" ng-click="modelCreate()"><b class="link-icon li-batinput"></b> <span class="vm">${lang.addProductDic}</span></a>       
    </div>
    <div class="up-detable">
      <table>
        <tr>
             <th>${lang.dicId}</th>
             <th>${lang.dicProduct}</th>
             <th>${lang.belong}</th>
             <th>${lang.adInfoOper}</th>
        </tr>
        <tr ng-repeat="row in resp.results">
             <td>{{row.id}}</td>
             <td>{{row.name}}</td>
             <td>{{row.type}}</td>
             <td>
             	   <a href="javascript:void(0)" ng-click="modelModify('{{row.id}}')" class="tb-edit"  ng-show="row.flag">${lang.mktInfoModify}</a>
             	   <a href="javascript:void(0)" ng-click="modelDel('{{row.id}}')"    class="tb-edit"  ng-show="row.flag">${lang.mktInfoDel}</a>
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
        
    </div>
    
    <div id="id_modify_mktproduct_dialog" style="display:none;font-size: 12px;">
	  <form name="mktDicInfoModifyForm" class="css-form" novalidate autocomplete="off">
	   <table border="0" class="css-table">
	
	   <tr>
	    <td>${lang.dicProduct}:&nbsp;</td>
	    <td>
	     <input type="text" name="mktDicInfoName" ng-model="mktDicInfoName" ng-trim="true" ng-minlength="1"  required/>
	     <span class="error">*&nbsp;</span>
	     <span class="error" ng-show="mktDicInfoModifyForm.mktDicInfoName.$invalid">${lang.input_null_validate}</span><br>
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
seajs.use(["modules/base/model", "modules/base/meta/mktProductDic"], function(model, MetaMktProductDic) {
    var meta =  MetaMktProductDic.instance("${vars.webRoot}", 'mktProductDic', ["mktProductDic","name"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:15
    });
});
</script>