<div class="upmc-wp" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
  
  <div class="upmob-filter">
    <br/>
    <p>${lang.mktUserDic}</p>
    <br/>
    <div class="upmc-controls upmcc-multi"> 
    </div>
    <div class="up-detable">
      <table>
        <tr>
             <th width="10%">${lang.account}</th>
             <th width="10%">${lang.name}</th>
             <th width="15%">${lang.department}</th>
             <th width="15%">${lang.phoneNum}</th>
             <th width="20%">${lang.email}</th>
             <th width="10%">${lang.belong}</th>
             <th width="20%">${lang.description}</th>
        </tr>
        <tr ng-repeat="row in resp.results">
             <td>{{row.account}}</td>
             <td>{{row.name}}</td>
             <td>{{row.department}}</td>
             <td>{{row.phoneNum}}</td>
             <td>{{row.email}}</td>
             <td>{{row.type}}</td>
             <td>{{row.description}}</td>
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

</div> <!-- end of up-detable -->
</div> <!-- end of upmob-filter -->    
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->



<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/mktUserDic"], function(model, MetaMktUserDic) {
    var meta = MetaMktUserDic.instance("${vars.webRoot}", 'mktUserDic', ["mktUserDic","name"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:15
    });
});
</script>