function alert_action ( alert_action )
{
     var baseUrl = window.location.protocol + "//" + window.location.host;

     if ( alert_action == 'success' )
     {
          $.toast( {
               heading: 'Login successfull!!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500,
               stack: 6,

          } );
     } else if ( alert_action == 'error' )
     {
          $.toast( {
               heading: 'Username or password are not match!!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'error',
               hideAfter: 3500
          } );
     } else if ( alert_action == 'sendMailfail' )
     {
          $.toast( {
               heading: 'Email not valid!!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'error',
               hideAfter: 3500
          } );

     }
     else if ( alert_action == 'sendMailsuccess' )
     {
          $.toast( {
               heading: 'We will send reset password mail to you!!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500
          } );
     }
     localStorage.removeItem( 'alert-action' );
}


