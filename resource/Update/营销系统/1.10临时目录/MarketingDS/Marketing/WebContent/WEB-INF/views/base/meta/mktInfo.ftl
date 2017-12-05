<div class="upmc-wp" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
  
    <#assign cr=(userRight?index_of("|c|") >= 0) />    
    <#assign dr=(userRight?index_of("|d|") >= 0) />    
    <#assign er=(userRight?index_of("|u|") >= 0) /> 
    
    <div class="upmob-filter">
    
         <#if cr>
            <a href="javascript:void(0)" id="evtedit_add" class="uf-right" ng-click="modelCreate()"><b class="link-icon li-create"></b> <span class="vm">添加营销活动</span></a>
         </#if>
        <span class="uf-item"><em>${lang.mktinfoName}：</em><input class="up-txt" type="text"  ng-keypress="pageClick(0)" ng-model="queryName"></span> 
        <a href="javascript:void(0)"  ng-click="pageClick(0)" class="btn-lightblue">${lang.query}</a>
        
    </div>
    
    
    <div class="up-detable">
      <table>
        <tr>
             <th width="10%">${lang.mktinfoId}</th>
             <th width="15%">${lang.mktinfoName}</th>
             <th width="15%">${lang.mktinfoProduct}</th>
             <th width="10%">${lang.mktinfoSalePoint}</th>
             <th width="10%">${lang.mktinfoPlatform}</th>
             <th width="10%">${lang.mktinfoDeliveryStartTime}</th>
             <th width="10%">${lang.mktinfoDeliveryEndTime}</th>
             <th width="20%">${lang.mktinfoOper}</th>
        </tr>
        <tr ng-repeat="row in resp.result">
             <td>{{row.mktinfoId}}</td>
             <td>{{row.mktinfoName}}</td>
             <td>{{row.mktinfoProduct}}</td>
             <td>{{row.mktinfoSalePoint}}</td>
             <td>{{row.mktinfoPlatform}}</td>
             <td>{{row.mktinfoDeliveryStartTime}}</td>
             <td>{{row.mktinfoDeliveryEndTime}}</td>
             <td>
                 <#if er>
                    <a href="javascript:void(0)" class="tb-edit"  ng-show="row.flag"  ng-click="modelModify('{{row.mktinfoId}}')">修改</a>
                 </#if>
                 
                  <a href="javascript:void(0)" class="tb-detail" ng-click="mktInfoDetail('{{row.mktinfoId}}')">详情</a>
                  
                 <#if dr>
                    <a href="javascript:void(0)" class="tb-del" ng-show="row.flag" ng-click="modelDel('{{row.mktinfoId}}')">删除</a>
                 </#if>
                 
                 <#if er>
                    <a href="javascript:void(0)" class="tb-adset" ng-show="row.flag" ng-click="mktEnableUser('{{row.mktinfoId}}')">广告位设置</a>
                 </#if>
                 
                 <#if er>
                    <a href="javascript:void(0)" class="tb-email" ng-show="row.flag" ng-click="mktEmail('{{row.mktinfoId}}')">邮件通知</a>
                 </#if>
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
    
    
    
    
    
<div id="id_mktUser_dialog" style="display:none;font-size: 12px;">
  <form name="mktInfoUserForm" class="css-form" novalidate autocomplete="off">
     <table class="listTab" style="width:600px;">
        <tr>
             <th width="20%">${lang.account}</th>
             <th width="30%">${lang.name}</th>
             <th width="30%">${lang.department}</th>
             <th width="40%">${lang.mktinfoRole}</th>
             
             
        </tr>
        <tr ng-repeat="row in mktUsers">
        
             <td>{{row.account}}</td>
             <td>{{row.name}}</td>
             <td>{{row.department}}</td>
             <td>
                 <input value="1" ng-model="row.flag" type="radio">全部
                 <input value="2" ng-model="row.flag" type="radio">没权限
             </td> 
        </tr>
    </table>
  </form>
</div>



<div id="id_mktEmail_dialog" style="display:none;font-size: 12px;">
  <form name="mktInfoEmailForm" class="css-form" novalidate autocomplete="off">
     <table class="listTab" style="width:600px;">
        <tr>
             <th width="20%"></th>
             <th width="20%">${lang.account}</th>
             <th width="30%">${lang.name}</th>
             <th width="30%">${lang.department}</th>
        </tr>
        <tr ng-repeat="row in mktEmailUsers">
        
             <td> 
                <input type="checkbox"  id="emailUsers" name="emailUsers" value="{{row.account}}" />&nbsp;&nbsp;
             </td>
             <td>{{row.account}}</td>
             <td>{{row.name}}</td>
             <td>{{row.department}}</td>
        </tr>
    </table>
  </form>
</div>


    
<div id="id_create_mktInfo_dialog" style="display:none;font-size: 12px;">
  <form name="mktInfoCreateForm" class="css-form" novalidate autocomplete="off">
   <table border="0" class="css-table">
   <tr>
    <td>${lang.mktinfoName}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" name="mktinfoName" ng-model="mktinfoName"  ng-maxlength="50" ng-trim="true"  required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="mktInfoCreateForm.mktinfoName.$invalid">${lang.mktinfoName_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktinfoProduct}:&nbsp;</td>
    <td>
     <select class="xyselect" ng-model="mktinfoProduct" ng-options="p.id as p.name for p in products" >
     </select>
     <span class="error" ng-show="mktInfoCreateForm.mktinfoProduct.$invalid">${lang.mktinfoProduct_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktinfoSalePoint}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" name="mktinfoSalePoint" ng-model="mktinfoSalePoint" ng-trim="true"  ng-maxlength="255"/>
     <span class="error" ng-show="mktInfoCreateForm.mktinfoSalePoint.$invalid">${lang.mktinfoSalePoint_validate}</span><br>
    </td>
   </tr>
   
   
   <tr>
    <td>${lang.mktinfoSlogan}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" name="mktinfoSlogan" ng-model="mktinfoSlogan"  ng-maxlength="255" />
     <span class="error" ng-show="mktInfoCreateForm.mktinfoSlogan.$invalid">${lang.mktinfoSlogan_validate}</span><br>
    </td>
   </tr>
   
   
   <tr>
    <td>${lang.mktinfoStrategicPosition}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" name="mktinfoStrategicPosition" ng-model="mktinfoStrategicPosition"  ng-maxlength="255"/>
     <span class="error" ng-show="mktInfoCreateForm.mktinfoStrategicPosition.$invalid">${lang.mktinfoStrategicPosition_validate}</span><br>
    </td>
   </tr>
   
     <tr>
    <td>${lang.mktinfoTargetPopulation}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" name="mktinfoTargetPopulation" ng-model="mktinfoTargetPopulation"  ng-maxlength="255"/>
     <span class="error" ng-show="mktInfoCreateForm.mktinfoTargetPopulation.$invalid">${lang.mktinfoTargetPopulation_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktinfoExpectedPrice}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" name="mktinfoExpectedPrice" ng-model="mktinfoExpectedPrice" ng-pattern="/^\d{0,}$/" />
     <span class="error" ng-show="mktInfoCreateForm.mktinfoExpectedPrice.$invalid">${lang.mktinfoExpectedPrice_validate}</span><br>
    </td>
   </tr>

   <tr>
    <td>${lang.mktinfoMarketPace}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" name="mktinfoMarketPace" ng-model="mktinfoMarketPace"  ng-maxlength="255" />
     <span class="error" ng-show="mktInfoCreateForm.mktinfoMarketPace.$invalid">${lang.mktinfoMarketPace_validate}</span><br>
    </td>
   </tr>
  
    <tr>
    <td>${lang.mktinfoPlatform}:&nbsp;</td>
    <td>
     <select class="xyselect" ng-model="mktinfoPlatform" ng-options="p.id as p.name for p in platforms" >
     </select>
     <span class="error" ng-show="mktInfoCreateForm.mktinfoPlatform.$invalid">>${lang.mktinfoPlatform_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktinfoBudget}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" name="mktinfoBudget" ng-model="mktinfoBudget" ng-pattern="/^\d{0,}$/" />
     <span class="error" ng-show="mktInfoCreateForm.mktinfoBudget.$invalid">${lang.mktinfoBudget_validate}</span><br>
    </td>
   </tr>


  <tr>
    <td>${lang.mktinfoPurchaseMethod}:&nbsp;</td>
    <td>
         
     <select class="xyselect" ng-model="mktinfoPurchaseMethod" ng-options="p.id as p.name for p in methods" >
     </select>
     
     <span class="error" ng-show="mktInfoCreateForm.mktinfoPurchaseMethod.$invalid">${lang.mktinfoPurchaseMethod_validate}</span><br>
    </td>
   </tr>

   <tr>
    <td>${lang.mktinfoReserveStartTime}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" readonly="true" id="mktinfoReserveStartTime" name="mktinfoReserveStartTime" ng-model="mktinfoReserveStartTime" />
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktinfoReserveEndTime}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" readonly="true" id="mktinfoReserveEndTime" name="mktinfoReserveEndTime" ng-model="mktinfoReserveEndTime"/>
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktinfoPurchaseStartTime}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" readonly="true" id="mktinfoPurchaseStartTime" name="mktinfoPurchaseStartTime" ng-model="mktinfoPurchaseStartTime"/>
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktinfoDeliveryStartTime}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" readonly="true" id="mktinfoDeliveryStartTime" name="mktinfoDeliveryStartTime" ng-model="mktinfoDeliveryStartTime" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="mktInfoCreateForm.mktinfoDeliveryStartTime.$invalid">${lang.mktinfoDeliveryStartTime_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktinfoDeliveryEndTime}:&nbsp;</td>
    <td>
     <input class="xytxt" type="text" readonly="true" id="mktinfoDeliveryEndTime" name="mktinfoDeliveryEndTime" ng-model="mktinfoDeliveryEndTime" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="mktInfoCreateForm.mktinfoDeliveryEndTime.$invalid">${lang.mktinfoDeliveryEndTime_validate}</span><br>
    </td>
   </tr>           

  </table>
  
  
  </form>
  </div>    
    

    
    
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->



<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/mktInfo"], function(model, MetaMktInfo) {
    var meta =  MetaMktInfo.instance("${vars.webRoot}", 'mktInfo', ["mktinfoId","name"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:15
    });
});
</script>