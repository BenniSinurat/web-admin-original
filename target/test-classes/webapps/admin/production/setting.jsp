<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

        <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Setting</h3>
              </div>

              <div class="title_right">
                <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
                  <div class="input-group">
                    <input type="text" class="form-control" placeholder="Search for...">
                    <span class="input-group-btn">
                      <button class="btn btn-default" type="button">Go!</button>
                    </span>
                  </div>
                </div>
              </div>
            </div>
            <div class="clearfix"></div>
          
            <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Profile</h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                        <ul class="dropdown-menu" role="menu">
                          <li><a href="#">Settings</a>
                          </li>
                        </ul>
                      </li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                  
                  <div class="x_content">
                     <form id="editProfile" data-parsley-validate class="form-horizontal form-label-left" action="/admin/editProfile" method="POST" modelAttribute="editmember" >
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Username :</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="username" value="${member.username}" readonly required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">Name :</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="name" name="name" value="${name}" disabled required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Email :</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="email" name="email" value="${email}" disabled required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      <div class="form-group">
                          <input id="username" name="username" value="${member.username}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
                          <!--input id="sessionID" name="sessionID" value="${sessionID}" class="date-picker form-control col-md-7 col-xs-12" type="hidden"-->
                      </div>
                      
                      <div class="ln_solid"></div>
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                          <button name="editprofile" id="editprofile" class="btn btn-primary" type="button">Edit</button>
		                  <button name="submitProfile" id="submitProfile" type="submit" class="btn btn-success" onClick="showInput();">Submit</button>
                        </div>
                      </div>
                    </form>
                    
                  </div>
                </div>
              </div>
            </div>
            
             <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Bank Account Transfer List</h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li>
                      	<a class="collapse-link">
                      		<i class="fa fa-chevron-up"></i>
                      	</a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" 
                        	role="button" aria-expanded="false">
                        	<i class="fa fa-wrench"></i>
                        </a>
                        <ul class="dropdown-menu" role="menu">
                          <li>
                          	<a href="#">Settings</a>
                          </li>
                        </ul>
                      </li>
                      <li>
                      	<a class="close-link">
                      		<i class="fa fa-close"></i>
                      	</a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                  
                  <div class="x_content">
                    <div align="right">
             			<button onClick="location.href='/admin/createBankAccount'" class="btn btn-success">Add New Account</button>
             		</div>
             	    <table id="acctransfertable" class="table table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
                      <thead>
                        <tr>
                          <th>Account No.</th>
                          <th>Account Name</th>
                          <th>Bank Name</th>
                          <th>Description</th>
                          <th>Action</th>
                        </tr>
                      </thead>                      
                    </table>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
            
                  </div>
                </div>
              </div>
            </div>

              </div>
            </div>
          </div>
        </div>
        <!-- /page content -->

<%@include file="footer.jsp" %>

<!--script>
	document.getElementById("name").value = localStorage.getItem("name");
	document.getElementById("email").value = localStorage.getItem("email");
	
	function showInput() {
		var name = document.getElementById("name").value;
		var email = document.getElementById("email").value;
		
	    localStorage.setItem("name", name);
		localStorage.setItem("email", email);
		
	    location.reload();
	    //localStorage.clear();
	}
</script-->

<script>
 		$("#acctransfertable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'lrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/accountTransferData",
       					 "data" : function ( d ) {
 		               	  	d.username = "${member.username}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "accountNo"
							}, {
								"data" : "accountName"
							}, {
								"data" : "bankName"
							}, {
								"data" : "description"
							},{
								"data" : "accountNo",
								"render" : function ( data, type, row ) {
                   					 return "<a href='detailBankAccount?username=${member.username}&accountNumber=" + data+"'>Detail</a>";;
               					 }
							}]
					});
	</script>

<script type='text/javascript'>
	   $('#editprofile').click(function() {
            if ($('#name').attr('disabled')) {
                $('#name').removeAttr('disabled');
                $('#email').removeAttr('disabled');
 				$('#bankaccount').removeAttr('disabled');
            }
            else {
                $('#name').attr({
                    'disabled': 'disabled'
            });
                $('#email').attr({
                    'disabled': 'disabled'
            });
            	 $('#bankaccount').attr({
                    'disabled': 'disabled'
            });
        };
    });
</script> 

<c:if test="${not empty fn:trim(notification)}">
<script type="text/javascript">
	$(function(){
 	 new PNotify({
        title: '${title}',
        text: '${message}',
        type: '${notification}',
        styling: 'bootstrap3'
        });
	});
	</script>
</c:if>

	</body>
</html>