// Function to generate a random CAPTCHA
function generateCaptcha() {
    const captchaLength = 6;
    let captcha = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (let i = 0; i < captchaLength; i++) {
        captcha += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return captcha;
}

// Function to validate the CAPTCHA
function validateCaptcha(input, captcha) {
    return input.toLowerCase() === captcha.toLowerCase();
}

const loginBtn = document.getElementById('loginBtn');
const createAccountBtn = document.getElementById('createAccountBtn');
const loginForm = document.getElementById('loginForm');
const createAccountForm = document.getElementById('createAccountForm');
const footer = document.getElementById('footer');

loginForm.classList.remove('hidden');
createAccountForm.classList.add('hidden');
footer.style.marginTop = '337px'; // Set initial margin for footer

loginBtn.addEventListener('click', () => {
    loginForm.classList.remove('hidden');
    createAccountForm.classList.add('hidden');
    footer.style.marginTop = '334px'; // Set margin for footer
});

createAccountBtn.addEventListener('click', () => {
    createAccountForm.classList.remove('hidden');
    loginForm.classList.add('hidden');
    footer.style.marginTop = '163px'; // Set margin for footer
});
