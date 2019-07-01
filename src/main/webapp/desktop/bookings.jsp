<%--
  Created by IntelliJ IDEA.
  User: marten
  Date: 26-6-19
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Bookings</title>
    <jsp:include page="head.jsp"/>
    <script src="/sqillsRoomBooking/scripts/filter-bookings.js"></script>
</head>
<body onload="filterBookings();">
    <jsp:include page="nav.jsp"/>
    <div class="row">
        <div class="col-md-2">
            <div class="filter-container">
                <h1>Filter Bookings</h1>
                <div>
                    <div class="filter-header">Email</div>
                    <input type="text" id="filter-email" value="" class="text-input"></div>
                <div>
                    <div class="filter-header">Title</div>
                    <input type="text" id="filter-title" value="" class="text-input"></div>
                <div>
                    <div class="filter-header">Lower Date Bound</div>
                    <input type="date" id="filter-startdate" value="" class="date-input"></div>
                <div>
                    <div class="filter-header">Upper Date Bound</div>
                    <input type="date" id="filter-enddate" value="" class="date-input"></div>
                <div>
                    <button onclick="filterBookings()">Filter</button></div>
            </div>
        </div>
        <div class="col-md-10">
            <div class="bookings">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Owner</th>
                            <th>Date</th>
                            <th>Start Time</th>
                            <th>End Time</th>
                        </tr>
                    </thead>
                    <tbody id="bookings-table-body">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
