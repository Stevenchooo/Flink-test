'use strict';

/**
 * Config for the router
 */
angular
		.module('app')
		.run(
				[ '$rootScope', '$state', '$stateParams',
						function($rootScope, $state, $stateParams) {
							$rootScope.$state = $state;
							$rootScope.$stateParams = $stateParams;
						} ])
		.config(
				[
						'$stateProvider',
						'$urlRouterProvider',
						'$httpProvider',
						function($stateProvider, $urlRouterProvider,
								$httpProvider) {

							$urlRouterProvider.otherwise('/home');

							$stateProvider
									.state(
											'home',
											{
												url : '/home',
												templateUrl : __urlRoot
														+ '/wda/tpl/home.html',
												permission : 'Edit',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __urlRoot
																				+ '/wda/js/controllers/home.js' ]);
															} ]
												}
											})

									 .state(
											'noright',
											{
												url : '/noright',
												templateUrl : __tplRoot
														+ 'tpl/noright.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __urlRoot
																				+ '/wda/js/controllers/home.js' ]);
															} ]
												}
											})

									.state(
											'app',
											{
												abstract : true,
												url : '/app',
												templateUrl : __urlRoot
														+ '/wda/tpl/app.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __urlRoot
																				+ '/wda/js/controllers/_app.js' ]);
															} ]
												}
											})

									.state(
											'app3',
											{
												abstract : true,
												url : '/app3',
												templateUrl : __tplRoot
														+ 'tpl/app3.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/app3.js' ]);
															} ]
												}
											})

									// .state(
									// 'app.dashboard-v1-index',
									// {
									// url : '/dashboard-v1-index',
									// templateUrl : __tplRoot
									// + 'tpl/app_dashboard_v1_index.html',
									// resolve : {
									// deps : [
									// 'uiLoad',
									// function(uiLoad) {
									// return uiLoad
									// .load([
									// __tplRoot
									// + 'js/controllers/home.js',
									// __tplRoot
									// + 'js/controllers/all_referrer.js' ]);
									// } ]
									// }
									// })

									.state(
											'app.website_summary',
											{
												url : '/website_summary',
												templateUrl : __urlRoot
														+ '/wda/tpl/website_summary.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __urlRoot
																				+ '/wda/js/controllers/website_summary.js' ]);
															} ]
												}

											})

									.state(
											'app.website_statistics_today',
											{
												url : '/website_statistics_today/{sid}',
												templateUrl : __urlRoot
														+ '/wda/tpl/website_statistics_today.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __urlRoot
																				+ '/wda/js/controllers/website_statistics_today.js' ]);
															} ]
												}

											})

									.state(
											'app.website_statistics_yesterday',
											{
												url : '/website_statistics_yesterday',
												templateUrl : __tplRoot
														+ 'tpl/website_statistics_yesterday.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(
																	$ocLazyLoad,
																	uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/website_statistics_yesterday.js' ]);
															} ]
												}
											})

									.state(
											'app.website_statistics_latest30',
											{
												url : '/website_statistics_latest30',
												templateUrl : __tplRoot
														+ 'tpl/website_statistics_latest30.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(
																	$ocLazyLoad,
																	uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/website_statistics_latest30.js' ]);
															} ]
												}
											})

									.state(
											'app.pages',
											{
												url : '',
												controller : 'PagesAnalyticsCtrl',
												templateUrl : __tplRoot
														+ 'tpl/pages_analytics.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/pages_analytics.js' ]);
															} ]
												}
											})

									.state(
											'app.pages.summary',
											{
												url : '/visited_pages/{periodTime}{huanBiDate}',
												templateUrl : __tplRoot
														+ 'tpl/pages_summary.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/pages_summary.js' ])
															} ]
												}
											})

									.state(
											'app.pages_trend',
											{
												url : '/pages_trend/{pageUrl}',
												templateUrl : __tplRoot
														+ 'tpl/pages_trend.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/pages_trend.js' ])
															} ]
												}
											})

									.state(
											'app.referrer',
											{
												url : '',
												controller : 'ReferrerCtrl',
												templateUrl : __tplRoot
														+ 'tpl/referrer.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/referrer-controller.js' ]);
															} ]
												}
											})

									.state(
											'app.referrer.all',
											{
												url : '/referrer/all/{periodTime}{huanBiDate}',
												templateUrl : __tplRoot
														+ 'tpl/referrer_all.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(
																	$ocLazyLoad,
																	uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/referrer_all.js' ])
															} ]
												}
											})

									.state(
											'app.referrer.promotion',
											{
												url : '/referrer/promotion/{periodTime}{huanBiDate}',
												templateUrl : __tplRoot
														+ 'tpl/referrer_promotion.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(
																	$ocLazyLoad,
																	uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/referrer_promotion.js' ])
															} ]
												}
											})

									.state(
											'app.referrer.outer',
											{
												url : '/referrer/outer/{periodTime}{huanBiDate}',
												templateUrl : __tplRoot
														+ 'tpl/referrer_outer.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(
																	$ocLazyLoad,
																	uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/referrer_outer.js' ])
															} ]
												}
											})

									.state(
											'app.referrer.direct',
											{
												url : '/referrer/direct/{periodTime}{huanBiDate}',
												templateUrl : __tplRoot
														+ 'tpl/referrer_direct.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(
																	$ocLazyLoad,
																	uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/referrer_direct.js' ])
															} ]
												}
											})

									.state(
											'app.referrer_trend',
											{
												url : '/referrer/trend/{refType}',
												templateUrl : __tplRoot
														+ 'tpl/referrer_trend.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(
																	$ocLazyLoad,
																	uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/referrer_trend.js' ])
															} ]
												}
											})

									.state(
											'app.referrer_trend2',
											{
												url : '/referrer/trend2/{refName}',
												templateUrl : __tplRoot
														+ 'tpl/referrer_trend2.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(
																	$ocLazyLoad,
																	uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/referrer_trend2.js' ])
															} ]
												}
											})

									.state(
											'app.area_distribution',
											{
												url : '/area/distribution',
												templateUrl : __tplRoot
														+ 'tpl/area_distribution.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/area_distribution.js' ])
															} ]
												}
											})

									.state(
											'app.area_userinsight',
											{
												url : '/area/userinsight',
												templateUrl : __tplRoot
														+ 'tpl/area_userinsight.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/area_userinsight.js' ])
															} ]
												}
											})

									.state(
											'app.area_trend',
											{
												url : '/area/trend/{provinceName}',
												templateUrl : __tplRoot
														+ 'tpl/area_trend.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/area_trend.js' ])
															} ]
												}
											})

									// 后台网站维护
									.state(
											'app3.website_management',
											{
												url : '/webiste_mgt',
												templateUrl : __tplRoot
														+ 'tpl/website_management.html',
												controller : 'WebsiteManagementCtrl',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([
																				__tplRoot
																						+ 'js/controllers/website_management.js',
																				__tplRoot
																						+ 'js/controllers/dataServices.js' ]);
															} ]
												}
											})

									// 后台用户管理
									.state(
											'app3.user_management',
											{
												url : '/user_mgt',
												templateUrl : __tplRoot
														+ 'tpl/user_management.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/user_management.js' ]);
															} ]
												}
											})

									// 后台角色管理
									.state(
											'app3.role_management',
											{
												url : '/role_mgt',
												templateUrl : __tplRoot
														+ 'tpl/role_management.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/role_management.js' ]);
															} ]
												}
											})

									// 后台群组管理
									.state(
											'app3.group_management',
											{
												url : '/group_mgt',
												templateUrl : __tplRoot
														+ 'tpl/group_management.html',
												resolve : {
													deps : [
															'uiLoad',
															function(uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/group_management.js' ]);
															} ]
												}
											})

									.state(
											'app.user.settings',
											{
												url : '/user/settings',
												controller : 'SettingsCtrl',
												templateUrl : __tplRoot
														+ 'tpl/user/settings/settings.html',
												resolve : {
													deps : [
															'$ocLazyLoad',
															'uiLoad',
															function(
																	$ocLazyLoad,
																	uiLoad) {
																return uiLoad
																		.load([ __tplRoot
																				+ 'js/controllers/all_referrer.js' ])
															} ]
												}
											})

						} ]).config(
				[ 'exDialogProvider', function(exDialogProvider) {
					exDialogProvider.setDefaults({
						// template: 'ngExDialog/commonDialog.html', //from
						// cache
						template : 'tpl/ngExDialog/commonDialog_0.html', // from
						// file
						width : '330px',
					// Below items are set within the provider. Any value set
					// here will overwrite that in the provider.
					// closeByXButton: true,
					// closeByClickOutside: true,
					// closeByEscKey: true,
					// appendToElement: '',
					// beforeCloseCallback: '',
					// grayBackground: true,
					// cacheTemplate: true,
					// draggable: true,
					// animation: true,
					// messageTitle: 'Information',
					// messageIcon: 'info',
					// messageCloseButtonLabel: 'OK',
					// confirmTitle: 'Confirmation',
					// confirmIcon: 'question',
					// confirmActionButtonLabel: 'Yes',
					// confirmCloseButtonLabel: 'No'
					});
				} ]);