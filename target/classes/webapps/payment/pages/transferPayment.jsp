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
	                  <p>Payment to OPTIMA</p>
			          <h3>Amount</h3>
	                  <p>${amount}</p>	
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
                <p>Below is Your <b>Payment Code</b>. <br/> Please do transfer before <b>${expiredAt}</b></p>
              	<div style="width: 90%; margin: 0px auto;">
              	<hr/>
              	<h1>${paymentCode}</h1>
              	<hr/>
              	</div>
                <p>Please note, we only <b>accept your payment</b> in accordance with the amount of payment as stated beside, we will <b>not process neither do refund</b> if your payment amount is not the exact amount as stated</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>  
	</body>
</html>