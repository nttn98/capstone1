document.addEventListener( "DOMContentLoaded", function ()
{
    // Select all rows in the cart
    var cartRows = document.querySelectorAll( '.row' );
    var checkoutButton = document.getElementById( 'checkoutBtn' );
    var insufficientStock = false;
    var cartIdElement = document.getElementById( 'cartId' );
    var cartId = cartIdElement ? cartIdElement.value : null;
    var mode = '';

    // Iterate over each row
    cartRows.forEach( function ( row )
    {
        // Get the productQuantity and quantity elements within the row
        var productPrice = row.querySelector( '#productPrice' );
        var subtotal = row.querySelector( '#subtotal' );

        var productQuantity = row.querySelector( '#productQuantity' );
        var quantity = row.querySelector( '#quantity' );
        var decreaseBtn = row.querySelector( '#decreaseBtn' );
        var increaseBtn = row.querySelector( '#increaseBtn' );
        var productId = row.querySelector( '#productId' );

        if ( productQuantity && quantity && decreaseBtn && increaseBtn )
        {
            // Get the values of the inputs
            var productQuantityValue = parseInt( productQuantity.value, 10 );
            var quantityValue = parseInt( quantity.value, 10 );
            var productIdValue = parseInt( productId.value );
            var productPriceValue = parseInt( productPrice.value );
            var subtotalValue = extractAndConvertValue( subtotal.textContent );
            // Add event listener to the decrease button
            decreaseBtn.addEventListener( 'click', function ()
            {
                if ( quantityValue > 0 )
                {
                    quantityValue--;
                    quantity.value = quantityValue;
                    subtotalValue = quantityValue * productPriceValue;
                    subtotal.textContent = formatMoney( subtotalValue );
                    mode = 'minus';
                    updateQuantity( quantityValue, row, productQuantityValue, productIdValue, mode );
                }
            } );

            // Add event listener to the increase button
            increaseBtn.addEventListener( 'click', function ()
            {
                if ( quantityValue < productQuantityValue )
                {
                    quantityValue++;
                    quantity.value = quantityValue;
                    quantity.value = quantityValue;
                    subtotalValue = quantityValue * productPriceValue;
                    subtotal.textContent = formatMoney( subtotalValue );

                    mode = 'plus';
                    updateQuantity( quantityValue, row, productQuantityValue, productIdValue, mode );
                }
            } );

            // Perform your check or action initially
            if ( productQuantityValue < quantityValue )
            {
                console.log( 'Insufficient stock for product:', row.querySelector( '#productName' ).innerText );
                row.classList.add( 'insufficient-stock' );
                insufficientStock = true;
            }
        }
    } );

    // Disable checkout button if there's insufficient stock
    if ( insufficientStock )
    {
        checkoutButton.disabled = true;
    }

    async function updateQuantity ( quantityValue, row, productQuantityValue, productIdValue, mode )
    {

        // Check if updated quantity is less than the product quantity
        if ( productQuantityValue < quantityValue )
        {
            row.classList.add( 'insufficient-stock' );
            insufficientStock = true;
        } else
        {
            row.classList.remove( 'insufficient-stock' );
            insufficientStock = false;
        }

        // Update the checkout button disabled state
        checkoutButton.disabled = insufficientStock;

        // Send fetch request to update quantity in the cart
        let res = await fetch( `/users/update-quantity-in-cart?productId=${ productIdValue }&quantity=${ quantityValue }&cartId=${ cartId }&mode=${ mode }`, {
            method: 'GET'
        } );
        if ( res.ok )
        {
            console.log( 'Quantity updated successfully.' );
            let totalInDbValue = await fetchTotalCartValue( cartId );
            if ( totalInDbValue !== null )
            {
                document.getElementById( "total" ).textContent = formatMoney( totalInDbValue );
            }

            if ( quantityValue == 0 )
            {
                window.location.href = "/";

            }
        }
    }
} );

function extractAndConvertValue ( priceString )
{
    // Ensure the input is treated as a string
    var str = String( priceString );

    // Remove the currency symbol and any non-numeric characters except the period
    var cleanedString = str.replace( /[^\d.]/g, '' );

    // Replace periods with empty strings to handle thousand separators
    cleanedString = cleanedString.replace( /\./g, '' );

    // Convert the cleaned string to a number
    var value = parseFloat( cleanedString );

    return value;
}

function formatMoney ( number )
{
    return number.toLocaleString( 'vi-VN', {
        style: 'currency', currency: 'VND'
    } );
}

async function fetchTotalCartValue ( cartId )
{
    let res = await fetch( `/users/total?cartId=${ cartId }`, { method: 'GET' } );
    return await res.json();
}