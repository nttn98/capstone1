function check ( event, curProductId, max )
{
    debugger
    curProductId = +curProductId;
    // let max = +$( "#productAvailable" ).text();
    max = +max;
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
        event.preventDefault();
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
