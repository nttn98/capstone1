function check ()
{
    debugger
    var curProductId = +$( "#curProductId" ).text();
    let max = +$( "#productAvailable" ).text();
    let add = 1;
    let current = +$( "#quantity" ).val();
    current = isNaN( current ) ? 0 : current;

    var productIds = document.querySelectorAll( "input[id='productId']" );
    var quantities = document.querySelectorAll( "input[id='quantity']" );

    for ( let i = 0; i < productIds.length; i++ )
    {
        let productIdElement = productIds[ i ];
        if ( curProductId !== +productIdElement.value )
        {
            current = 0;
        } else
        {
            let quantityElement = quantities[ i ];
            current = +quantityElement.value;
            break;
        }
    }
    if ( max >= current + add )
    {
        return true;
    }
    else
    {
        $.toast( {
            heading: 'Error',
            text: `Quantity cannot exceed available stock. Available: ${ max }`,
            position: 'top-right',
            loaderBg: '#ff6849',
            icon: 'error',
            hideAfter: 2500,
            stack: 6,
        } );
        return false;
    }
};
$( document ).ready( function ()
{
    $( "#add-to-cart-link" ).click( function ( event )
    {
        if ( !check() )
        {
            event.preventDefault();
        }
    } );
} );