<%@page contentType="text/html; charset=utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="content">
    <div id="content-header">
        <div id="breadcrumb">
            <a href="<c:url value="/index"/>" title="Go to Home" class="tip-bottom">
                <i class="icon-home"></i> 
                Home
            </a> 
            <a href="<c:url value="/account/list"/>" class="tip-bottom">
                Quản lý tài khoản Quản trị
            </a>
            <a href="#" class="current">
                Tài khoản Quản trị đã xóa</a>
        </div>
    </div>
    <div class="container-fluid">
        <div ng-app="recycleAccountApp" ng-controller="recycleAccountCtrl" class="row-fluid" 
             ng-init="maxRow = '20'; crPage = '1'; key = ''; phone = ''; email = ''; status = '127';">
            <div class="span12">
                <div class="widget-box">
                    <div class="widget-title"> <span class="icon"> <i class="icon-align-justify"></i> </span>
                        <h5 style="color: red">Tìm kiếm tải khoản đã xóa</h5>
                        <span class="fr" style="padding: 9px">
                            <a href="<c:url value="/account/list"/>" style="color: #0088cc;font-weight: bold;">
                                Danh sách tài khoản 
                                <img width="16" src="<c:url value="/images/list.png"/>">
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
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="widget-box">
                    <div class="widget-title"> 
                        <span class="icon"> 
                            <i class="icon-th"></i> 
                        </span>
                        <h5 style="color: red">
                            Danh sách tài khoản đã xóa 
                            <span id="result_model" class="callback_info">${result.message}</span>
                            <span class="callback_info">{{result.message}}</span>
                        </h5>
                        <div class="fr" style="padding: 3px">
                            Hiển thị 
                            <select ng-model="maxRow" ng-change="updateMaxRow()">
                                <option value="20">20 dòng</option>
                                <option value="50">50 dòng</option>
                                <option value="100">100 dòng</option>
                            </select>
                        </div>
                    </div>
                    <div class="widget-content nopadding">
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Username</th>
                                    <th>Full Name</th>
                                    <th>Mô tả</th>
                                    <th>Phone</th>
                                    <th>Email</th>
                                    <th>Create Date</th>
                                    <th>Create By</th>
                                    <th>Trạng thái</th>
                                    <th>Undodel/Xóa</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr ng-repeat="recycle in datas">
                                    <td>{{$index + 1}}</td>
                                    <td>{{recycle.username}}</td>
                                    <td>{{recycle.fullname}}</td>
                                    <td>{{recycle.description}}</td>
                                    <td>{{recycle.phone}}</td>
                                    <td>{{recycle.email}}</td>
                                    <td ng-bind="formatDateTime(recycle.createDate)"></td>
                                    <td>{{recycle.createBy}}</td>
                                    <td class="img-center">
                                        <img ng-if="recycle.status === ${status.ACTIVE}" width="24" src="<c:url value="/images/active.png"/>" alt="active">
                                        <img ng-if="recycle.status === ${status.UNACTIVE}" width="24" src="<c:url value="/images/inactive.png"/>" alt="inactive">
                                        <img ng-if="recycle.status === ${status.LOCK}" width="24" src="<c:url value="/images/lock.png"/>" alt="block">
                                    </td>
                                    <td style="width: 70px">
                                        <a ng-click="undoDelete(recycle.id)" href="" title="undoDel" >
                                            <img width="24" src="<c:url value="/images/redo-128.png"/>" alt="Undo">
                                        </a>
                                        <a ng-click="deleteforEver(recycle.id)" href="" title="Delete">
                                            <img src="<c:url value="/images/remove.png"/>" alt="Delete">
                                        </a> 
                                    </td>
                                </tr>
                                <tr ng-if="totalRow > maxRow">
                                    <td colspan="13">
                                        <div class="pagination aalternate fr">
                                            <ul uib-pagination total-items="totalRow" max-size="5" ng-model="crPage" ng-change="pageChanged()" items-per-page="maxRow" boundary-links="true" num-pages="numPages"></ul>
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
<script src="<c:url value='/controller/AccountRecycle.js'/>"></script>