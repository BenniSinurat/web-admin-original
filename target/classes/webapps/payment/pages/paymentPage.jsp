<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	 <title>OPTIMA Payment Page</title>
 	 <link rel="stylesheet" href="css/bootstrap.min.css" />
 	 <link rel="stylesheet" href="css/optima.css" />
 	 <link rel="stylesheet" href="css/main.min.css" />
    </head>
    <body>
     <div class="container">
      <div class="main-content">
        <div class="row-5">
          <div class="col-sm-4 hidden-xs">
            <div class="box clearfix">
     			   <div class="invoice">
	                <div class="content">
	                  <h3 style="margin-top: 0">Payment To</h3>
	                  <h2>${eventName}</h2>
			          <br />	
	                  <h3>Description</h3>
	                  <p>${description}</p>
			          <h3>Amount</h3>
	                  <p>${amount}</p>	
	                  <br />	
	                  <br />	
	         	      <br />	
	                  <br />	
	                  <br />	
	                  <br />	
	                 <h3>Powered by OPTIMA</h3>
	                </div><!--.content-->
	              </div><!--.invoice-->
	        </div><!--.box-->
          </div><!--.col-->
          <div class="box col-sm-8">
            <div class="login">
              <div class="head">
              <div class="logo"><img src="image/logo.png"></div>
              </div>
              <div class="midbar hidden-xs"></div>
              <div class="main">
                <p>Please provide your name, phone number, email and click <b>SUBMIT</b> to get your <b>Payment Code</b>. Or click <b>Cancel</b> to close this page</p>
              	 <form id="payment" name="ecommform" role="form" class="form-horizontal" action="/payment/submitTransferForm" method="post" modelAttribute="transfer">
	                  <div class="form-group">
	                    <label class="col-sm-3 control-label">Name</label>
	                    <div class="col-sm-9">
	                      <input id="name" name="name" class="form-control" type="text" value="">
	                    </div>
	                  </div>
	                  <div class="form-group">
	                    <label class="col-sm-3 control-label">Mobile No.</label>
	                    <div class="col-sm-9">
	                      <input id="msisdn" name="msisdn" class="form-control" type="text" value="">
	                    </div>
	                  </div>
	                  <div class="form-group">
	                    <label class="col-sm-3 control-label">Email</label>
	                    <div class="col-sm-9">
	                    	<input type="text" id="email" name="email" class="form-control">
	                    </div>
	                  </div>
	                  <div class="form-group">
	                    <div class="col-sm-offset-3 col-sm-6">
		  	              <input id="ticketID" name="ticketID" class="form-control" type="hidden" value="${ticketID}">
	    			 	  <button type="submit" class="btn btn-success">Submit</button>
		  				  <button type="button" onclick="quitBox('quit');" class="btn btn-success">Cancel</button>
	                    </div>
	                  </div>
	              </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>  
    
    <script>
    function quitBox(cmd) { 
      if (cmd=='quit') {
           open(location, '_self').close(); 
       }
       return false;
     }   
    </script>
	</body>
</html>