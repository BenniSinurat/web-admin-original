<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- Meta, title, CSS, favicons, etc. -->
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<title>OPTIMA Dashboard</title>
	
	<!-- Bootstrap -->
	<link href="vendors/bootstrap/dist/css/bootstrap.min.css"
		rel="stylesheet">
	<!-- Font Awesome -->
	<link href="vendors/font-awesome/css/font-awesome.min.css"
		rel="stylesheet">
	<!-- NProgress -->
	<link href="vendors/nprogress/nprogress.css" rel="stylesheet">
	<!-- Animate.css -->
	<link href="vendors/animate.css/animate.min.css" rel="stylesheet">
	
	<!-- Custom Theme Style -->
	<link href="build/css/custom.min.css" rel="stylesheet">
	<!-- Google Recaptcha v2 -->
	<script src='https://www.google.com/recaptcha/api.js'></script>
	<script type="text/javascript">
		//<![CDATA[
			var tlJsHost = ((window.location.protocol == "https:") ? "https://secure.comodo.com/" : "http://www.trustlogo.com/");
			document.write(unescape("%3Cscript src='" + tlJsHost + "trustlogo/javascript/trustlogo.js' type='text/javascript'%3E%3C/script%3E"));
		//]]>
	</script>
</head>

<body class="login
	<div>
		<a class="hiddenanchor" id="signup"></a> <a class="hiddenanchor"
			id="signin"></a>

		<div class="login_wrapper">
			<div class="animate form login_form">
				<section class="login_content">
					<form method="POST" action="/admin/submitLogin" commandName="index"
						modelAttribute="login">
						<h1>Login Form</h1>
						
						<div>
							<span style="border:none; box-shadow: none; font-size: 12; color:red;">${status}</span>
							<input type="text" class="form-control" placeholder="Username"
								name="username" required="" />
						</div>
						<div>
							<input type="password" class="form-control"
								placeholder="Password" name="password" required="" />
						</div>
						<div class="g-recaptcha" data-sitekey="${key}"></div>
						<br/>
						<div>
							<button type="submit" class="btn btn-default submit">Log In</button>
							<button type="reset" class="btn btn-default submit">Reset</button>
						</div>
						<div class="clearfix"></div>

						<div class="separator">
			            <div class="clearfix"></div>
		                <br />

         		       <div>
                		  <h1><i class="fa fa-paw"></i> OPTIMA</h1>
       		           <p><i class="fa fa-copyright"></i> 2017 Jatelindo Perkasa Abadi</p>
       		           <a href="https://ssl.comodo.com/ev-ssl-certificates.php" id="comodoTL">
       		           <img src="images/comodo.png" style="width:80px;height:33px;"></a>
            	    </div>
    			</div>
					</form>
				</section>
			</div>
		</div>
	</div>
	<script language="JavaScript" type="text/javascript">
		TrustLogo("https://optima-s.co.id/admin/login", "SC5", "none");
	</script>

</body>
</html>
