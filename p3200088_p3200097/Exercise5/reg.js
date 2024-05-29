window.onload = function(){

    let form = document.getElementById('registrationForm'); // Assuming the form has an id of 'registrationForm'

    // Accessing form elements by name
    
    let edtFullName = form.elements['fullName'];
    let edtEmail = form.elements['email'];
    let edtPassword = form.elements['password'];
    let edtConfirmPassword = form.elements['confirmPassword'];

   

    form.addEventListener('submit', function(event) {
        // Validate form fields and handle submission
        // Use Constraint Validation API for basic validation
        if (!this.checkValidity()) {
            event.preventDefault(); // Prevent form submission if validation fails
        }
        
        registerUser();
    });


    function checkPasswordMatch() {
        let password = document.getElementById('password');
        let confirmPassword = document.getElementById('confirmPassword');
        let errorDiv = document.getElementById('passwordMatchError');
    
        if (password !== confirmPassword) {
            errorDiv.textContent = 'Passwords do not match.';
        } else {
            errorDiv.textContent = '';
        }
    }




    function registerUser() {
        // Retrieve user input
        let fullName = edtFullName;
         let email = edtEmail;
         let password = edtPassword; 
         let confirmPassword = edtConfirmPassword;
         
         checkPasswordMatch() ;
        
        // Check password security requirements
        if (!isPasswordSecure(password)) {
            console.error('Password does not meet security requirements.');
            
            return;
        } else if (!isPasswordMatching(password, confirmPassword)) {
            console.error('Passwords do not match.');
           
            return;
        }

       
    }

    function isPasswordSecure(password) {
        // Example security requirements:
        // Minimum length: 8 characters
        // At least one uppercase letter, one lowercase letter, and one digit

        let minLength = 8;
        let hasUpperCase = /[A-Z]/.test(password);
        let hasLowerCase = /[a-z]/.test(password);
        let hasDigit = /\d/.test(password);

        return (
            password.length >= minLength &&
            hasUpperCase &&
            hasLowerCase &&
            hasDigit
        );
    }
    function isPasswordMatching(password,confirmPassword) {
        return password === confirmPassword;
    }
}