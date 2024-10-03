function generateCaptcha() {
    const captchaLength = 6;
    let captcha = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (let i = 0; i < captchaLength; i++) {
        captcha += characters.charAt(Math.floor(Math.random() * characters.length));
    }
    return captcha;
}

function validateCaptcha(input, captcha) {
    return input.trim().toLowerCase() === captcha.trim().toLowerCase();
}

function handleLogin() {
    let captcha = generateCaptcha();
    document.getElementById('captchaDisplay').textContent = captcha;

    document.getElementById('loginForm').addEventListener('submit', async function(event) {
        event.preventDefault();

        const captchaInput = document.getElementById('captchaInput').value;

        if (!validateCaptcha(captchaInput, captcha)) {
            alert("Invalid CAPTCHA. Please try again.");
            captcha = generateCaptcha();
            document.getElementById('captchaDisplay').textContent = captcha;
            return;
        }

        const data = {
            phoneNumber: document.getElementById('phone_number').value,
            password: document.getElementById('loginPassword').value
        };

        const response = await fetch('/customer_login/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (response.ok) {
            alert(result.message);
            window.location.href = '/customer_login/user_dashboard'; // Redirect to dashboard
        } else {
            alert(result.message);
        }
    });
}

function handleSignup() {
    document.getElementById('createAccountForm').addEventListener('submit', async function(event) {
        event.preventDefault();

        const data = {
            firstName: document.getElementById('first_name').value,
            lastName: document.getElementById('last_name').value,
            phoneNumber: document.getElementById('phone_number').value,
            password: document.getElementById('newPassword').value,
            confirmPassword: document.getElementById('confirmPassword').value
        };

        const response = await fetch('/customer_login/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();
        alert(result.message);
        if (response.ok) {
            window.location.href = '/customer_login';
        } else {
            alert(result.message);
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    if (document.cookie.split(';').some((item) => item.trim().startsWith('auth_token='))) {
        window.location.href = '/customer_login/user_dashboard';
    } else {
        handleLogin();
    }

    const loginBtn = document.getElementById('loginBtn');
    const createAccountBtn = document.getElementById('createAccountBtn');
    const loginForm = document.getElementById('loginForm');
    const createAccountForm = document.getElementById('createAccountForm');
    const footer = document.getElementById('footer');

    if (loginBtn && createAccountBtn && loginForm && createAccountForm) {
        loginBtn.addEventListener('click', () => {
            loginForm.classList.remove('hidden');
            createAccountForm.classList.add('hidden');
            handleLogin();
        });

        createAccountBtn.addEventListener('click', () => {
            createAccountForm.classList.remove('hidden');
            loginForm.classList.add('hidden');
            handleSignup();
        });
    } else {
        console.error('Some elements are missing in the DOM');
    }
});
