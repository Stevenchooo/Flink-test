<div ms-controller="ACCOUNT_LIST" style="padding:0 10px 0 10px;">
	<div style="text-align:right;">
		<span ms-if="rights['c']" ms-click="showCreate" class="oprEnabled">{{tags.doCreate}}</span>
		<span ms-if="rights['u']" ms-click="showModify" ms-class="oprEnabled:lineNo>=0">{{tags.doModify}}</span>
		<span ms-if="rights['d']" ms-click="doRemove" ms-class="oprEnabled:lineNo>=0">{{tags.doRemove}}</span>
	</div>
	<table width="100%">
	  <tr>
	    <th>{{tags.segAccount}}</th>
	    <th>{{tags.createTime}}</th>
	    <th>{{tags.expireTime}}</th>
	    <th>{{tags.phoneNum}}</th>
	    <th>{{tags.name}}</th>
	    <th>{{tags.email}}</th>
	  </tr>
	  <tr ms-repeat-el="dataList" ms-class="activeTr:$index === lineNo" ms-click="selectLine($event, $index)">
	    <td>{{el.account}}</td>
	    <td>{{el.createTime}}</td>
	    <td>{{el.expireTime}}</td>
	    <td>{{el.phoneNum}}</td>
	    <td>{{el.name}}</td>
	    <td>{{el.email}}</td>
	  </tr>
	</table>
	<div id="__pageInfo"></div>

	<!-- dialog divs -->
	<div class="dialogDiv" id="createDiv">
		<form id="createForm">
			<table>
			<tr><th>{{tags.segAccount}}</th><td><input name="account" ms-duplex="createData.account"></td></tr>
			<tr><th>{{tags.password}}</th><td><input name="password" ms-duplex="createData.password"></td></tr>
			<tr><th>{{tags.name}}</th><td><input name="name" ms-duplex="createData.name"></td></tr>
			<tr><th>{{tags.phoneNum}}</th><td><input name="phoneNum" ms-duplex="createData.phoneNum"></td></tr>
			<tr><th>{{tags.email}}</th><td><input name="email" ms-duplex="createData.email"></td></tr>
			<tr>
				<th><img id="createVerifyCodeImg" src="${vars.webRoot}/verifyCode.htm?w=70&h=22" title="点击换一张"></th>
				<td><input name="verifyCode" ms-duplex="createData.verifyCode"></td>
			</tr>
			</table>
		</form>
	</div>
	
	<div class="dialogDiv" id="modifyDiv">
		<form id="modifyForm">
			<input type="hidden" name="account" ms-duplex="modifyData.account">
			<table>
			<tr><th>{{tags.segAccount}}</th><td>{{modifyData.account}}</td></tr>
			<tr><th>{{tags.name}}</th><td><input name="name" ms-duplex="modifyData.name"></td></tr>
			<tr><th>{{tags.phoneNum}}</th><td><input name="phoneNum" ms-duplex="modifyData.phoneNum"></td></tr>
			<tr><th>{{tags.email}}</th><td><input name="email" ms-duplex="modifyData.email"></td></tr>
			</table>
		</form>
	</div>
</div>

<script language="javascript">
$(document).ready(function() {
	seajs.use(["account"], function(module) {
		module.init(${rights!"{}"}, "ACCOUNT_LIST");
	});
});
</script>