
$( document ).ready( function ()
{
    $( '.formatPrice' ).each( ( i, e ) =>
    {
        let val = + $( e ).text();
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
    $( "#password" ).val( '' );
    $( "#newPassword" ).val( '' );
    $( "#confirmPassword" ).val( '' );
}

function showRecoverPassword ()
{
    $( "#CustomerLoginForm" ).addClass( "d-none" );
    $( "#RecoverPasswordForm" ).removeClass( "d-none" );
    $( "#modeChangePw" ).val( 'user' );

}

function closeRecoverPassword ()
{
    $( "#RecoverPasswordForm" ).addClass( "d-none" );
    $( "#CustomerLoginForm" ).removeClass( "d-none" );
    $( "#register" ).addClass( "d-none" );
    $( "#recoveryPwForm input" ).val( '' );

}

function showRegister ()
{
    $( "#register" ).removeClass( "d-none" );
    $( "#CustomerLoginForm" ).addClass( "d-none" );
    $( "#modeRegister" ).val( 'user' );
}

function closeRegister ()
{
    $( "#register" ).addClass( "d-none" );
    $( "#CustomerLoginForm" ).removeClass( "d-none" );
    $( "#registerForm input" ).val( '' );

}

$( '.closeMask' ).click( ( e ) =>
{
    if ( e.target.classList.contains( 'closeMask' ) )
    {
        $( 'button.close.close-mask' ).click();
        closeRecoverPassword();
        closeRegister();
    }
    //close noti
    var dropdown = document.getElementById( 'notiDropdown' );
    if ( dropdown !== null && dropdown.style.display === 'block' )
    {

        dropdown.style.opacity = '0';
        setTimeout( function ()
        {
            dropdown.style.display = 'none';
            resetNotifications();
        }, 200 );
        window.location.reload();
    }

    // close descript
    var description = document.querySelector( '.description-extend' );
    if ( description !== null && description.style.display === 'block' )
    {
        setTimeout( function ()
        {
            description.style.display = 'none';
        }, 200 );
    }
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
    closecart();
    $( '.closeLogin' ).fadeIn();
    $( '.closeMask' ).fadeIn();
}

function closeLogin ()
{
    $( '.login' ).removeClass( "w-25" );

    $( '.closeLogin' ).fadeOut();
    $( '.closeMask' ).fadeOut();
}

function showcart ()
{
    $( '.cart' ).addClass( "w-43" );
    $( '.closeLogin' ).fadeIn();
    $( '.closeMask' ).fadeIn();
}

function closecart ()
{
    $( '.cart' ).removeClass( "w-43" );
    var temPage = document.getElementById( 'page' );
    var page = temPage ? temPage.value : null;
    if ( page === 'checkout' )
    {
        location.reload();
    }
    $( '.closeMask' ).fadeOut();
}

function showNoti ( userId )
{
    var dropdown = document.getElementById( 'notiDropdown' );
    if ( dropdown.style.display === 'none' || dropdown.style.display === '' )
    {
        dropdown.style.display = 'block';
        dropdown.style.opacity = '1';
        $( '.closeMask' ).fadeIn();

        fetch( `/readNotifications?userId=${ userId }`, {
            credentials: 'include'
        } )
            .then( response =>
            {
                if ( !response.ok )
                {
                    throw new Error( 'Network response was not ok' );
                }
                return response.text();
            } )
            .then( data =>
            {
                console.log( 'Notifications marked as read' );
            } )
            .catch( error =>
            {
                console.error( 'There was a problem with the fetch operation:', error );
            } );
    } else
    {
        dropdown.style.opacity = '0';
        setTimeout( function ()
        {
            dropdown.style.display = 'none';

        }, 200 );
    }
}


function showDescriptionExtend ()
{
    var description = document.querySelector( '.description-extend' );
    description.style.display = 'block';
    $( '.closeMask' ).fadeIn();
}

function closeDescription ()
{
    var description = document.querySelector( '.description-extend' );
    setTimeout( function ()
    {
        description.style.display = 'none';
    }, 200 );
    $( '.closeMask' ).fadeOut();
}

var showMoreNotiBtn = document.getElementById( 'show-more-noti' );

if ( showMoreNotiBtn )
{
    showMoreNotiBtn.addEventListener( 'click', () =>
    {
        const hiddenItems = document.querySelectorAll( '.hidden-noti' );
        hiddenItems.forEach( item =>
        {
            item.classList.remove( 'hidden-noti' );
        } );
        showMoreNotiBtn.style.display = 'none';
        showLessNotiBtn.style.display = 'block';
    } );
}

var showLessNotiBtn = document.getElementById( 'show-less-noti' );
if ( showLessNotiBtn )
{
    showLessNotiBtn.addEventListener( 'click', () =>
    {
        const allItems = document.querySelectorAll( '.noti-mess-head' );
        allItems.forEach( ( item, index ) =>
        {
            if ( index > 2 )
            {
                item.classList.add( 'hidden-noti' );
            }
        } );
        showMoreNotiBtn.style.display = 'block';
        showLessNotiBtn.style.display = 'none';
    } );
}

function resetNotifications ()
{
    var allItems = document.querySelectorAll( '.noti-mess-head' );
    allItems.forEach( ( item, index ) =>
    {
        if ( index > 2 )
        {
            item.classList.add( 'hidden-noti' );
        }
    } );
    if ( showMoreNotiBtn )
    {
        showMoreNotiBtn.style.display = 'block';
    }
    if ( showLessNotiBtn )
    {
        showLessNotiBtn.style.display = 'none';
    }
}
