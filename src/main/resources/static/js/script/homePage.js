$( document ).ready( function ()
{
    $( '.formatPrice' ).each( ( i, e ) =>
    {
        let val = +( $( e ).text() );
        $( e ).text( formatMoney( val ) );
    } );

    function formatMoney ( number )
    {
        return number.toLocaleString( 'vi-VN', {
            style: 'currency', currency: 'VND'
        } );
    }
} );

function showPass ()
{
    var p = document.getElementById( "password" );
    if ( p.type === "password" )
    {
        p.type = "text";
    } else
    {
        p.type = "password";
    }
}
function showNewPass ()
{
    var p = document.getElementById( "newPassword" );
    if ( p.type === "password" )
    {
        p.type = "text";
    } else
    {
        p.type = "password";
    }
}
function showConfirmPassword ()
{
    var p = document.getElementById( "confirmPassword" );
    if ( p.type === "password" )
    {
        p.type = "text";
    } else
    {
        p.type = "password";
    }
}
function onChange ()
{
    var password = document.querySelector( 'input[id=newPassword]' );
    var confirm = document.querySelector( 'input[id=confirmPassword]' );
    if ( confirm.value === password.value )
    {
        confirm.setCustomValidity( '' );
    } else
    {
        confirm.setCustomValidity( 'Passwords do not match' );
    }
}
function showChangePassword ()
{
    $( "#changePassword" ).removeClass( "d-none" );
    $( "#profileForm" ).addClass( "d-none" );

}

function closeChangePassword ()
{
    $( "#changePassword" ).addClass( "d-none" );
    $( "#profileForm" ).removeClass( "d-none" );

}
function showRecoverPassword ()
{
    $( "#CustomerLoginForm" ).addClass( "d-none" )
    $( "#RecoverPasswordForm" ).removeClass( "d-none" );
}
function closeRecoverPassword ()
{
    $( "#RecoverPasswordForm" ).addClass( "d-none" );
    $( "#CustomerLoginForm" ).removeClass( "d-none" )
    $( "#register" ).addClass( "d-none" );

}

function showRegister ()
{
    $( "#register" ).removeClass( "d-none" );
    $( "#CustomerLoginForm" ).addClass( "d-none" );
}
function closeRegister ()
{
    $( "#register" ).addClass( "d-none" );
    $( "#CustomerLoginForm" ).removeClass( "d-none" );

}

$( '.closeMask' ).click( ( e ) =>
{
    if ( e.target.classList.contains( 'closeMask' ) )
        $( 'button.close.close-mask' ).click();
    closeRecoverPassword();
} );

$( document ).ready( function ()
{
    $( window ).scroll( function ()
    {
        if ( $( this ).scrollTop() > 100 )
        {
            $( '#topButton' ).fadeIn();
        } else
        {
            $( '#topButton' ).fadeOut();
        }
    } );

    $( '#topButton' ).click( function ()
    {
        $( 'html, body' ).animate( { scrollTop: 0 }, 400 );
        return false;
    } );
} );

function showUserProfile ()
{
    $( '.userProfile' ).addClass( "w-25" );
    $( '.closeLogin' ).fadeIn();
    $( '.closeMask' ).fadeIn();
}
function closeUserProfile ()
{
    $( '.userProfile' ).removeClass( "w-25" );
    $( '.closeMask' ).fadeOut();
}

function showLogin ()
{
    $( '.login' ).addClass( "w-25" );
    $( '.closeLogin' ).fadeIn();
    $( '.closeMask' ).fadeIn();
}
function closeLogin ()
{
    $( '.login' ).removeClass( "w-25" )
    $( '.closeLogin' ).fadeOut();
    $( '.closeMask' ).fadeOut();
}

function showcart ()
{
    $( '.cart' ).addClass( "w-32" )
    $( '.closeLogin' ).fadeIn();
    $( '.closeMask' ).fadeIn();
}
function closecart ()
{
    $( '.cart' ).removeClass( "w-32" )
    $( '.closeMask' ).fadeOut();
}
function quantityPlus ( e )
{
    let quantity = $( e ).closest( 'div' ).find( 'input' );
    quantity.val( +quantity.val() + 1 );
}
function quantityMinus ( e )
{
    let quantity = $( e ).closest( 'div' ).find( 'input' );
    quantity.val( +quantity.val() - 1 );
}
alert_action(/*[[${ alert }]]*/ )
