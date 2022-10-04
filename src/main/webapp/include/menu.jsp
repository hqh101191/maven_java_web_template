<%@ page contentType="text/html; charset=utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="header">
    <h1><a href="/">Home</a></h1>
</div>

<div id="user-nav" class="navbar navbar-inverse">
    <ul class="nav">        
        <li class="">
            <a title="" href="<c:url value="/logout"/>">
                <i class="icon icon-share-alt"></i> 
                <span class="text">Logout</span>
            </a>
        </li>
    </ul>
</div>

<div id="sidebar"> 
    <ul>
        <li class="submenu"> 
            <a href="<c:url value="/index"/>">
                <i class="icon icon-th-list"></i> 
                <span>Trang chủ</span> 
            </a>
        </li>

        <li class="submenu">
            <a href="#">
                <i class="icon icon-th-list"></i> 
                <span>
                    ${Lang['menu.system.main']}
                </span>
                <span class="label label-important">
                    2
                </span>
            </a>
            <ul>
                <li><a href="<c:url value="/account/list"/>">Quản lý Tài khoản hệ thống</a></li>
                <li><a href="<c:url value="/user-action/list"/>">Lịch sử người dùng thao tác</a></li>
            </ul>
        </li>
    </ul>
</div>
