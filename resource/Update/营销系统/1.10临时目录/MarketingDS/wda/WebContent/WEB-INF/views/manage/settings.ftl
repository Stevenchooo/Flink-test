<style>
table.modify {
    width:60%;
	border:0;
	margin-left:30px;
}
table.modify td,th {
    font-size:14px;
    height:25px;
	border:0;
}
</style>
<div ms-controller="SETTINGS" style="padding:0 10px 0 10px;">
	<div style="margin-left:30px;">
		<h2>{{tags.changePassword}}</h2>
		<table class="modify">
		<form id="changePwdForm">
			<tr><th>{{tags.oldPassword}}</th><td><input name="oldPassword" type="password" ms-duplex="userData.oldPassword"></td></tr>
			<tr><th>{{tags.newPassword}}</th><td><input name="newPassword" type="password" id="newPassword" ms-duplex="userData.newPassword"></td></tr>
			<tr><th>{{tags.cfmPassword}}</th><td><input name="confirmPassword" type="password" ms-duplex="userData.confirmPassword"></td></tr>
		</form>
		<tr><th></th><td><button ms-click="doChangePassword">{{tags.doModify}}</button></td></tr>
		</table>
	</div>
	<br><br>
	<div style="margin-left:30px;">
		<h2>{{tags.accountSet}}</h2>
		<table class="modify">
		<form id="changeInfoForm">
			<tr><th>{{tags.name}}</th><td><input name="name" ms-duplex="userData.name"></td></tr>
			<tr><th>{{tags.phoneNum}}</th><td><input name="phoneNum" ms-duplex="userData.phoneNum"></td></tr>
			<tr><th>{{tags.email}}</th><td><input name="email" ms-duplex="userData.email"></td></tr>
		</form>
		<tr><th></th><td><button ms-click="doChangeInfo">{{tags.doModify}}</button></td></tr>
		</table>
	</div>
</div>

<script language="javascript">
$(document).ready(function() {
	seajs.use(["settings"], function(module) {
		module.init("SETTINGS", "${__account}");
	});
});
</script>