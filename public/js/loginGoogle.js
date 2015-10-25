$( document ).ready(function() {
    console.log( "ready!" );
    
    function onSignIn(googleUser) {
    	  var profile = googleUser.getBasicProfile();
    	  console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    	  console.log('Name: ' + profile.getName());
    	  console.log('Image URL: ' + profile.getImageUrl());
    	  console.log('Email: ' + profile.getEmail());
    	  console.log('Token: ' + profile.getA)
    	  
    	  var email = profile.getEmail();
    	  
    	}
    
    
    
    
    
    
});



//function onSuccess(googleUser) {
//	console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
//	var email = googleUser.getBasicProfile().getEmail();
//	
//	console.log(email);
//	var nuevaRuta = "http://localhost:9000/nuevo/"+email;
//	console.log(nuevaRuta);
//	  //window.location.assign( nuevaRuta );
//	
//	var id_token = googleUser.getAuthResponse().id_token;
//	var xhr = new XMLHttpRequest();
//	xhr.open('POST', 'http://localhost:9000/google/validate');
//	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
//	xhr.onload = function() {
//	  console.log('Signed in as: ' + xhr.responseText);
//	  document.write(xhr.responseText);
//	  document.close();
//	};
//	xhr.send('idtoken=' + id_token);
//	
//
//}
//function onFailure(error) {
//	console.log(error);
//}
//function renderButton() {
//	gapi.signin2.render('my-signin', {
//		'scope' : 'email',
//		'width' : 200,
//		'height' : 50,
//		'longtitle' : true,
//		'theme' : 'dark',
//		'onsuccess' : onSuccess,
//		'onfailure' : onFailure
//	});
//}