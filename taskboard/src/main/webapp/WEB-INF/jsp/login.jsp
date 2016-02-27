<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta name="google-signin-client_id" content="651917160024-8lmki27a1rkjpbkctbrd28jcigoojebf.apps.googleusercontent.com">
</head>
<body>
SIGN IN : <div id="my-signin2"></div>
<script>
  function onSuccess(googleUser) {
    console.log('Logged in as: ' + googleUser.getBasicProfile().getName());
    var id_token = googleUser.getAuthResponse().id_token;

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/login');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
      console.log('Signed in as: ' + xhr.responseText);
      location.reload();
    };
    xhr.send('tokenId=' + id_token);
  }

  function onFailure(error) {
    console.log(error);
  }

  function renderButton() {
    gapi.signin2.render('my-signin2', {
      'scope': 'profile email',
      'width': 240,
      'height': 50,
      'longtitle': true,
      'theme': 'dark',
      'onsuccess': onSuccess,
      'onfailure': onFailure
    });
  }
</script>

<script src="https://apis.google.com/js/platform.js?onload=renderButton" async defer></script>
</body>
</html>