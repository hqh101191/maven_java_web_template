<%@page language="java" contentType="text/html; charset=utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="content">
    <div id="content-header">
        <div id="breadcrumb">
            <a href="<c:url value="/index"/>" title="Go to Home" class="tip-bottom">
                <i class="icon-home"></i> 
                ${Lang['generic.home']}
            </a> 
            <a href="#" class="current">
                ${Lang['system.account.subMenu']}
            </a>
        </div>
    </div>
    <div class="container-fluid">
        <div ng-app="listAccountApp" ng-controller="listAccountCtrl" class="row-fluid"
             ng-init="maxRow = '20'; crPage = '1'; key = ''; phone = ''; email = ''; status = '127';">
            <div class="span12">
                <div class="widget-box">
                    <div class="widget-title"> <span class="icon"><i class="icon-align-justify"></i> </span>
                        <h5>${Lang['system.account.search.title']}</h5>
                        <span class="fr" style="padding: 9px">
                            <a href="<c:url value="/account/recycle"/>" style="color: red;font-weight: bold;">
                                ${Lang['generic.deleted']} 
                                <img width="16" src="<c:url value="/images/recycle.png"/>">
                            </a>
                        </span>
                    </div>
                    <div class="widget-content nopadding">
                        <form class="form-horizontal">
                            <div class="control-group">
                                <label class="control-label">${Lang['system.account.search.user']} </label>
                                <div class="controls">
                                    <input class="text-input" ng-model="key" type="text">
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    ${Lang['generic.phone']}
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <input class="text-input" ng-model="phone" type="text">
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    Email
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <input class="text-input" ng-model="email" type="text">
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    ${Lang['generic.status']}
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <select class="text-input" ng-model="status">
                                        <option value="127">Tất cả</option>
                                        <option value="1">Kích hoạt</option>
                                        <option value="0">Chưa kích hoạt</option>
                                        <option value="2">Khóa</option>
                                    </select>
                                </div>
                            </div>
                            <div class="control-group">
                                <div class="controls">
                                    <div ng-click="reloadFilter()" class="btn btn-success">${Lang['generic.search']}</div>
                                    &nbsp;&nbsp;&nbsp;
                                    <input type="reset" ng-click="reset()" class="btn btn-warning" name="Reset Form" value="Làm mới"/>
                                    &nbsp;&nbsp;&nbsp;
                                    <input class="btn btn-primary" onclick="location.href = '<c:url value="/account/create" />'" value="${Lang['generic.addNew']}" type="button">
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="widget-box">
                    <div class="widget-title"> <span class="icon"> <i class="icon-th"></i> </span>
                        <h5>${Lang['system.account.detail.title']} <span id="result_model" class="callback_info">${result.message}</span>
                            <span class="callback_info">{{result.message}}</span></h5>
                        <div class="fr" style="padding: 3px">
                            ${Lang['generic.display']} 
                            <select ng-model="maxRow" ng-change="updateMaxRow()">
                                <option value="5">5 ${Lang['generic.line']}</option>
                                <option value="20">20 ${Lang['generic.line']}</option>
                                <option value="50">50 ${Lang['generic.line']}</option>
                                <option value="100">100 ${Lang['generic.line']}</option>
                            </select>
                        </div>
                    </div>
                    <div class="widget-content nopadding">
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>${Lang['generic.index']}</th>
                                    <th>${Lang['generic.userName']}</th>
                                    <th>${Lang['generic.fullName']}</th>
                                    <th>${Lang['generic.phone']}</th>
                                    <th>${Lang['generic.email']}</th>
                                    <th>${Lang['generic.createDate']}</th>
                                    <th>${Lang['generic.createBy']}</th>
                                    <th>${Lang['generic.status']}</th>
                                    <th>${Lang['generic.edit']}/${Lang['generic.delete']}</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr ng-repeat="one in datas">
                                    <td>{{($index + 1)}}</td>
                                    <td>{{one.username}}</td>
                                    <td>{{one.fullname}}</td>
                                    <td>{{one.phone}}</td>
                                    <td>{{one.email}}</td>
                                    <td ng-bind="formatDateTime(one.createdAt)"></td>
                                    <td>{{one.createdBy}}</td>
                                    <td class="img-center">
                                        <img ng-if="one.del" width="24" src="<c:url value="/images/recycle.png"/>" alt="recycle">
                                        <img ng-if="one.status === ${status.ACTIVE} || one.status === ${status.ALL}" width="24" src="<c:url value="/images/active.png"/>" alt="active">
                                        <img ng-if="one.status === ${status.UNACTIVE}" width="24" src="<c:url value="/images/inactive.png"/>" alt="inactive">
                                        <img ng-if="one.status === ${status.LOCK}" width="24" src="<c:url value="/images/lock.png"/>" alt="block">
                                    </td>
                                    <td style="width: 70px">
                                        <div>
                                            <a href="${pageContext.request.contextPath}/account/edit?id={{one.id}}" >
                                                <img src="<c:url value="/images/edit.png"/>" title="Edit">
                                            </a>
                                            <a ng-click="delete(one.id, one.username)" href="" title="Delete">
                                                <img src="<c:url value="/images/remove.png"/>" title="Delete">
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                                <tr ng-if="totalRow > maxRow">
                                    <td colspan="13">
                                        <div class="pagination aalternate fr">
                                            <ul uib-pagination total-items="totalRow" ng-model="crPage" ng-change="pageChanged()" max-size="5" items-per-page="maxRow" boundary-links="true" num-pages="numPages"></ul>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<c:url value='/js/jquery.min.js'/>"></script>
<script src="<c:url value='/js/jquery.blockUI.js'/>"></script>
<script src="<c:url value='/js/angular-ui-select2/select2o.js'/>"></script>
<script src="<c:url value='/js/angular-ui-select2/angular.js'/>"></script>
<script src="<c:url value='/js/angular-ui-select2/select2.js'/>"></script>
<script src="<c:url value='/js/jquery.alerts.js'/>"></script>
<script src="<c:url value='/js/ui-bootstrap-tpls-2.1.3.min.js'/>"></script>
<script src="<c:url value='/controller/Account.js'/>"></script>