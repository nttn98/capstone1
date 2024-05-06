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
    $( '.changePass' ).addClass( 'w-25' );
    $( '.profile' ).removeClass( "w-25" );
}
function closeChangePass ()
{
    $( '.changePass' ).removeClass( "w-25" );
    $( '.closeMask' ).fadeOut();

}
function showProfile ()
{
    $( '.profile' ).addClass( "w-25" );
    $( '.changePass' ).removeClass( "w-25" );
    $( '.closeMask' ).fadeIn();
}
function closeProfile ()
{
    $( '.profile' ).removeClass( "w-25" );
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