function onSuccess(googleUser) {
	console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
	var email = googleUser.getBasicProfile().getEmail();
	
	console.log(email);
	var nuevaRuta = "http://localhost:9000/nuevo/"+email;
	console.log(nuevaRuta);
	  window.location.assign( nuevaRuta );

}
function onFailure(error) {
	console.log(error);
}
function renderButton() {
	gapi.signin2.render('registrar', {
		'scope' : 'email',
		'width' : 200,
		'height' : 50,
		'longtitle' : true,
		'theme' : 'dark',
		'onsuccess' : onSuccess,
		'onfailure' : onFailure
	});
}