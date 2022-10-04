<%@page language="java" contentType="text/html; charset=utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="content">
    <div id="content-header">
        <div id="breadcrumb">
            <a href="<c:url value="/index"/>" title="Go to Home" class="tip-bottom">
                <i class="icon-home"></i> 
                Home
            </a>
            <a href="#" class="current">
                Hành động của người dùng
            </a>
        </div>
    </div>
    <div class="container-fluid">
        <div ng-app="UserActionApp" ng-controller="UserActionCtrl" class="row-fluid" 
             ng-init="maxRow = '20'; crPage = '1'; user_name = ''; table_action = ''; action_type = ''; result_act = '';">
            <div class="span12">
                <div class="widget-box">
                    <div class="widget-title"> <span class="icon"><i class="icon-align-justify"></i> </span>
                        <h5>Tìm kiếm :</h5>                        
                    </div>
                    <div class="widget-content nopadding">
                        <form class="form-horizontal">
                            <div class="control-group">
                                <label class="control-label"> Tên người dùng:</label>
                                <div class="controls">
                                    <input class="text-input" ng-model="user_name" type="text">
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label"> Bảng truy cập:</label>
                                <div class="controls">
                                    <input class="text-input" ng-model="table_action" type="text">
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label"> Kiểu hành động:</label>
                                <div class="controls">
                                    <input class="text-input" ng-model="action_type" type="text">
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label"> Kết quả:</label>
                                <div class="controls">
                                    <select ui-select2 style="width: 150px;vertical-align: middle" ng-model="result_act">
                                        <option value=""> Tất cả </option>
                                        <option value="SUCCESS"> SUCCESS </option>
                                        <option value="FAIL"> FAIL </option>
                                        <option value="NO_RIGHT"> NO RIGHT </option>
                                    </select>
                                </div>
                            </div>
                            <div class="control-group">
                                <div class="controls">
                                    <div ng-click="reloadFilter()" class="btn btn-success">Tìm kiếm</div>
                                    &nbsp;&nbsp;&nbsp;
                                    <input type="reset" class="btn btn-warning" ng-click="reset()" name="Reset Form" value="Làm mới"/>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="widget-box">
                    <div class="widget-title"> <span class="icon"><i class="icon-th"></i></span>
                        <h5>Hành động của người dùng <span id="result_model" class="callback_info">${result.message}</span>
                            <span class="callback_info">{{result.message}}</span></h5>
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
                                    <th>Tên người dùng</th>
                                    <th>Địa chỉ IP</th>
                                    <th>Hành động URL</th>
                                    <th>Bảng truy cập</th>
                                    <th>Kiểu hành động</th>
                                    <th>Ngày hành động</th>
                                    <th>Kết quả</th>
                                    <th>Thông tin</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr ng-repeat="oneAct in datas">
                                    <td class="img-center">{{($index + 1)}}</td>
                                    <td>{{oneAct.userName}}</td>
                                    <td>{{oneAct.userIp}}</td>
                                    <td>{{oneAct.urlAction}}</td>
                                    <td>{{oneAct.tableAction}}</td>
                                    <td>{{oneAct.actionType}}</td>
                                    <td ng-bind="formatDateTime(oneAct.actionDate)"></td>
                                    <td>{{oneAct.result}}</td>
                                    <td>{{oneAct.info}}</td>
                                </tr>
                                <tr ng-if="totalRow > maxRow"> 
                                    <td colspan="13">
                                        <div class="pagination aalternate fr">
                                            <ul uib-pagination total-items="totalRow" ng-model="crPage" ng-change="pageChanged()" max-size="5" items-per-page="maxRow" class="" boundary-link-numbers="true"  num-pages="numPages"></ul>
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
<script src="<c:url value='/js/angular-ui-select2/select2o.js'/>"></script> 
<script src="<c:url value='/js/angular-ui-select2/angular.js'/>"></script>
<script src="<c:url value='/js/angular-ui-select2/select2.js'/>"></script>
<script src="<c:url value='/js/ui-bootstrap-tpls-2.1.3.min.js'/>"></script>
<script src="<c:url value='/js/jquery.alerts.js'/>"></script>
<script src="<c:url value='/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/js/jquery.blockUI.js'/>"></script>
<script src="<c:url value='/js/maruti.popover.js'/>"></script>
<script src="<c:url value='/controller/UseAction.js'/>"></script>
