
    // Donation Form submit
    const donationForm = document.querySelector('.confirm-button');

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
    const response = await fetch('http://localhost:8080/auth/donations', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify(donationDTO)
});

    const data = await response.json();
    alert(data.message); // Show response message
} catch (error) {
    console.error('Error:', error);
    alert('Something went wrong. Please try again.');
}
});

