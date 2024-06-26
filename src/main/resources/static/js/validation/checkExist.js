function checkUsername ()
{
    var username = document.getElementById( 'usernameInput' ).value.trim();
    var usernameError = document.getElementById( 'usernameError' );
    var submitButton = document.getElementById( 'submitRegister' );

    fetch( '/checkUsernameAvailability?username=' + encodeURIComponent( username ) )
        .then( response => response.json() )
        .then( isAvailable =>
        {
            if ( isAvailable )
            {
                usernameError.textContent = '';
                submitButton.disabled = false; // Enable the submit button
                submitButton.classList.remove( 'btn-danger' ); // Remove red color class
                submitButton.classList.add( 'btn-info' ); // Add default color class
            } else
            {
                usernameError.textContent = 'Username already taken';
                submitButton.disabled = true; // Disable the submit button
                submitButton.classList.remove( 'btn-info' ); // Remove default color class
                submitButton.classList.add( 'btn-danger' ); // Add red color class
            }
            var emailError = document.getElementById( 'emailError' );
            if ( usernameError.textContent.length == 0 && emailError.textContent.length != 0 )
            {
                submitButton.disabled = true; // Disable the submit button
                submitButton.classList.remove( 'btn-info' ); // Remove default color class
                submitButton.classList.add( 'btn-danger' ); // Add red color class
            }
        } )
        .catch( error =>
        {
            console.error( 'Error:', error );
            usernameError.textContent = 'Error fetching user data';
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
            var usernameError = document.getElementById( 'usernameError' );
            if ( usernameError.textContent.length != 0 && errorSpan.textContent.length == 0 )
            {
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
function checkIdcard ()
{
    var idcard = document.getElementById( "idcard" ).value.trim();
    var errorSpan = document.getElementById( 'idcardError' );
    var submitButton = document.getElementById( 'submitRegister' );

    fetch( '/checkIdcardAvailability?idcard=' + encodeURIComponent( idcard ) )
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
                errorSpan.textContent = 'Idcard already taken';
                submitButton.disabled = true; // Disable the submit button
                submitButton.classList.remove( 'btn-info' ); // Remove default color class
                submitButton.classList.add( 'btn-danger' ); // Add red color class
            }
            var usernameError = document.getElementById( 'usernameError' );
            var emailError = document.getElementById( 'emailError' );
            if ( emailError.textContent.length != 0 || usernameError.textContent.length != 0 && errorSpan.textContent.length == 0 )
            {
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