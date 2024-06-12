function alert_action ( alert_action )
{
    if ( alert_action == 'success' )
    {
        $.toast( {
            heading: 'successfully !',
            position: 'top-right',
            loaderBg: '#ff6849',
            icon: 'success',
            hideAfter: 3500,
            stack: 6,

        } );
    } else if ( alert_action == 'error' )
    {
        $.toast( {
            heading: 'Error',
            position: 'top-right',
            loaderBg: '#ff6849',
            icon: 'error',
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
    }
    localStorage.removeItem( 'alert-action' )
}


