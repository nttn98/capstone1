function checkUsername ()
{
    var username = document.getElementById( "usernameInput" ).value.trim();
    var errorSpan = document.getElementById( 'usernameError' );
    var submitButton = document.getElementById( 'submitRegister' );

    fetch( '/checkUsernameAvailability?username=' + encodeURIComponent( username ) )
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


function checkExistEmail ()
{
    var originalEmail = document.getElementById( "originalEmail" ).value.trim();
    var email = document.getElementById( "emailInput" ).value.trim();
    var errorSpan = document.getElementById( 'emailError' );
    var submitButton = document.getElementById( 'submitRegister' );

    fetch( '/checkEmailAvailability?email=' + encodeURIComponent( email ) + '&originalEmail=' + encodeURIComponent( originalEmail ) )
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
function checkName ()
{
    var name = document.getElementById( "name" ).value.trim();
    var errorSpan = document.getElementById( 'nameError' );
    var submitButton = document.getElementById( 'submitForm' );

    fetch( '/checkProductNameAvailability?name=' + encodeURIComponent( name ) )
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
                errorSpan.textContent = 'Name already taken';
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