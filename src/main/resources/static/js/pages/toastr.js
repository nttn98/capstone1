function alert_action ( alert_action )
{
     if ( alert_action == 'true' )
     {
          $.toast( {
               heading: 'Successfully!',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'success',
               hideAfter: 3500,
               stack: 6
          } );
     } else if ( alert_action == 'false' )
     {
          $.toast( {
               heading: 'Error',
               position: 'top-right',
               loaderBg: '#ff6849',
               icon: 'error',
               hideAfter: 3500
          } );
     }
     localStorage.removeItem( 'alert-action' )
}


