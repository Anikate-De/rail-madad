function handleLogin() {
    document.getElementById('loginForm').addEventListener('submit', async function(event) {
        event.preventDefault();

        const data = {
            id: document.getElementById('formGroupExampleInput').value,
            password: document.getElementById('formGroupExampleInput2').value
        };

        const response = await fetch('/officers/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();
        if (response.ok) {
            alert(result.message);
            window.location.href = "/officerDashboard";
        } else {
            alert(result.message);
        }
    });
}


document.addEventListener('DOMContentLoaded', () => {
    if (document.cookie.split(';').some((item) => item.trim().startsWith('token='))) {
        window.location.href = '/officerDashboard';
    }
});
