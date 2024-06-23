function toggleButtons ()
{
    var buttonContainer = document.getElementById( 'buttonContainer' );
    var toggleButton = document.getElementById( 'toggleButton' );
    if ( buttonContainer.style.display === 'none' )
    {
        buttonContainer.style.display = 'flex';
    } else
    {
        buttonContainer.style.display = 'none';
    }
}

function showChangePass ()
{
    $( '.changePass' ).addClass( 'w-22' );
    $( '.profile' ).removeClass( "w-22" );
}

function closeChangePass ()
{
    $( '.changePass' ).removeClass( "w-22" );
    $( '#oldPassword' ).val( '' );
    $( '#newPassword' ).val( '' );
    $( '.closeMask' ).fadeOut();

}
function showChangePassAdmin ()
{
    $( '.changePassAdmin' ).addClass( 'w-22' );
    $( '.closeMask' ).fadeIn();

}

function closeChangePassAdmin ()
{
    $( '.changePassAdmin' ).removeClass( "w-22" );
    $( '#oldPassword' ).val( '' );
    $( '#newPassword' ).val( '' );
    $( '.closeMask' ).fadeOut();

}
function showProfile ()
{
    $( '.profile' ).addClass( "w-22" );
    $( '.changePass' ).removeClass( "w-22" );
    $( '.closeMask' ).fadeIn();
}
function closeProfile ()
{
    $( '.profile' ).removeClass( "w-22" );
    $( '.closeMask' ).fadeOut();
}

$( '.closeMask' ).click( ( e ) =>
{
    if ( e.target.classList.contains( 'closeMask' ) )
        $( 'button.close.close-mask' ).click();
} );

$( document ).ready( () =>
{
    $( '.formatPrice' ).each( ( i, e ) =>
    {
        let val = parseFloat( $( e ).text().replace( /,/g, '' ) );
        $( e ).text( formatMoney( val ) );
    } );

    function formatMoney ( number )
    {
        return number.toLocaleString( 'vi-VN', {
            style: 'currency', currency: 'VND'
        } );
    }
} );