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
alert_action(/*[[${alert}]]*/ )
