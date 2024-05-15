function checkUserUsername ()
{
    var username = document.getElementById( "usernameInput" ).value.trim();
    var errorSpan = document.getElementById( 'usernameError' );
    var submitButton = document.getElementById( 'submitRegister' );

    fetch( '/checkUserUsernameAvailability?username=' + encodeURIComponent( username ) )
        .then( response => response.json() )
        .then( isAvailable =>
        {
            if ( isAvailable )
            {
                errorSpan.textContent = '';
                submitButton.disabled = false; // Enable the submit button
                submitButton.classList.remove( 'btn-danger' ); // Remove red color class
                submitButton.classList.add( 'btn-info' ); // Add default color class
            } else
            {
                errorSpan.textContent = 'Username already taken';
                submitButton.disabled = true; // Disable the submit button
                submitButton.classList.remove( 'btn-info' ); // Remove default color class
                submitButton.classList.add( 'btn-danger' ); // Add red color class
            }
        } )
        .catch( error =>
        {
            console.error( 'Error:', error );
            errorSpan.textContent = 'Error fetching user data';
        } );
}


function checkUserEmail ()
{
    var email = document.getElementById( "emailInput" ).value.trim();
    var errorSpan = document.getElementById( 'emailError' );
    var submitButton = document.getElementById( 'submitRegister' );

    fetch( '/checkUserEmailAvailability?email=' + encodeURIComponent( email ) )
        .then( response => response.json() )
        .then( isAvailable =>
        {
            if ( isAvailable )
            {
                errorSpan.textContent = '';
                submitButton.disabled = false; // Enable the submit button
                submitButton.classList.remove( 'btn-danger' ); // Remove red color class
                submitButton.classList.add( 'btn-info' ); // Add default color class
            } else
            {
                errorSpan.textContent = 'Email already taken';
                submitButton.disabled = true; // Disable the submit button
                submitButton.classList.remove( 'btn-info' ); // Remove default color class
                submitButton.classList.add( 'btn-danger' ); // Add red color class
            }
        } )
        .catch( error =>
        {
            console.error( 'Error:', error );
            errorSpan.textContent = 'Error fetching user data';
        } );
}
