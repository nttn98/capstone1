function alert_action ( alert_action )
{
     if ( alert_action == 'success' )
     {
          $.toast( {
               heading: 'Login successfully !!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500,
               stack: 6,

          } );
     } else if ( alert_action == 'error' )
     {
          $.toast( {
               heading: 'Username or password are not match !!',
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
     } else if ( alert_action == 'sendContact' )
     {
          $.toast( {
               heading: 'We will reply via your email!!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500
          } );
     } else if ( alert_action == "logout" )
     {
          $.toast( {
               heading: 'Logout successfully !!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500
          } );
     } else if ( alert_action == "edit" )
     {
          $.toast( {
               heading: 'Edit successfully !!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500
          } );
     } else if ( alert_action == "errorEdit" )
     {
          $.toast( {
               heading: 'Email already used !!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'error',
               hideAfter: 3500
          } );
     }
     else if ( alert_action == "recoverPass" )
     {
          $.toast( {
               heading: ' You recovered your password successfully !!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500
          } );
     } else if ( alert_action == "successRegister" )
     {
          $.toast( {
               heading: ' Register successfully !!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500
          } );
     } else if ( alert_action == "changePass" )
     {
          $.toast( {
               heading: ' Changed password successfully !!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500
          } );
     }
     else if ( alert_action == "paymentSuccess" )
     {
          $.toast( {
               heading: ' Order successfully !!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500
          } );
     }
     else if ( alert_action == "addToCartS" )
     {
          $.toast( {
               heading: ' Add to cart successfully !!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500
          } );
     }

     localStorage.removeItem( 'alert-action' );
}


