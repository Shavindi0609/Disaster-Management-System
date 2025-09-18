// Donation Form submit
const donationForm = document.querySelector('.confirm-button');

// -------------------- Centralized fetch --------------------
async function fetchWithToken(url, options = {}) {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        alert("⚠️ Session expired. Please login again.");
        localStorage.clear();
        window.location.href = "login.html";
        return Promise.reject("No token found");
    }

    options.headers = {
        ...options.headers,
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
    };

    const res = await fetch(url, options);
    if (!res.ok) {
        const text = await res.text();
        throw new Error(`HTTP ${res.status}: ${text}`);
    }
    return res.json();
}
donationForm.addEventListener('click', async (e) => {
    e.preventDefault();

    // Form fields
    const name = document.querySelector('input[placeholder="Your Name"]').value;
    const email = document.querySelector('input[placeholder="Your Email"]').value;
    const company = document.querySelector('input[placeholder="Company name (if applicable)"]').value;
    const amount = document.querySelector('input[name="amount"]:checked')?.value || document.querySelector('input[placeholder="Amount"]').value;
    const paymentMethod = document.querySelector('.payment-method.active').textContent;
    const cardNumber = document.querySelector('#cardNumber')?.value || '';
    const cardName = document.querySelector('#cardName')?.value || '';
    const expiry = document.querySelector('#expiry')?.value || '';
    const cvv = document.querySelector('#cvv')?.value || '';
    const receiveUpdates = document.querySelector('input[type="checkbox"]').checked;

    // Build DTO object
    const donationDTO = {
        name,
        email,
        company,
        donationAmount: amount,
        paymentMethod,
        cardNumber,
        cardName,
        expiry,
        cvv,
        receiveUpdates
    };

    try {
        // ✅ Use fetchWithToken for Authorization header
        const data = await fetchWithToken('http://localhost:8080/api/donations', {
            method: 'POST',
            body: JSON.stringify(donationDTO)
        });

        alert(data.message || "Donation submitted!");

        // ✅ Reset fields
        document.querySelector('input[placeholder="Your Name"]').value = '';
        document.querySelector('input[placeholder="Your Email"]').value = '';
        document.querySelector('input[placeholder="Company name (if applicable)"]').value = '';
        document.querySelectorAll('input[name="amount"]').forEach(r => r.checked = false);
        document.querySelector('input[placeholder="Amount"]').value = '';
        document.querySelector('#cardNumber').value = '';
        document.querySelector('#cardName').value = '';
        document.querySelector('#expiry').value = '';
        document.querySelector('#cvv').value = '';
        document.querySelector('input[type="checkbox"]').checked = false;

        document.querySelectorAll('.payment-method').forEach(pm => pm.classList.remove('active'));

    } catch (error) {
        console.error('Error:', error);
        alert('Something went wrong: ' + error.message);
    }
});
