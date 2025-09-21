
    const BASE = "http://localhost:8080/api";

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

    // -------------------- Page Navigation --------------------
    function showPage(pageId, el) {
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    document.getElementById(pageId).classList.add('active');

    document.querySelectorAll('.sidebar a').forEach(a => a.classList.remove('active'));
    if (el) el.classList.add('active');

    document.getElementById("pageTitle").innerText =
    pageId.charAt(0).toUpperCase() + pageId.slice(1);

    if (pageId === "volunteers") loadVolunteers();
    if (pageId === "donors") loadDonors();
    if (pageId === "users") loadUsers();
}

    // -------------------- Volunteers --------------------
    async function loadVolunteers() {
    try {
    const result = await fetchWithToken(`${BASE}/volunteers`);
    const container = document.getElementById("volunteerList");
    container.innerHTML = "";
    result.data.forEach(v => {
    const card = document.createElement("div");
    card.className = "item-card";
    card.innerHTML = `
                <h4>${v.name}</h4>
                <p><strong>Email:</strong> ${v.email}</p>
                <p><strong>Phone:</strong> ${v.phone || "-"}</p>
                <p><strong>Skills:</strong> ${v.skills || "-"}</p>
                <button onclick="openEditModal('volunteer', ${v.id}, '${v.name}', '${v.email}', '${v.phone || ''}', '${v.skills || ''}')">Edit</button>
                <button onclick="deleteVolunteer(${v.id})">Delete</button>
            `;
    container.appendChild(card);
});
} catch (err) {
    console.error("Error loading volunteers:", err);
}
}

    document.getElementById("addVolunteerForm").addEventListener("submit", async e => {
    e.preventDefault();
    const vol = {
    name: volName.value,
    email: volEmail.value,
    phone: volPhone.value,
    skills: volSkills.value
};
    try {
    await fetchWithToken(`${BASE}/volunteers`, { method:"POST", body: JSON.stringify(vol) });
    loadVolunteers();
    e.target.reset();
        alert("✅ Volunteer added successfully!");
} catch(err) {
    console.error("Error adding volunteer:", err);
}
});

    async function deleteVolunteer(id) {
    if (!confirm("Are you sure you want to delete this volunteer?")) return;
    try {
    await fetchWithToken(`${BASE}/volunteers/${id}`, { method:"DELETE" });
    loadVolunteers();
} catch(err) { console.error("Error deleting volunteer:", err); }
}

    // -------------------- Donors --------------------
    async function loadDonors() {
    try {
    const result = await fetchWithToken(`${BASE}/donors`);
    const container = document.getElementById("donorList");
    container.innerHTML = "";
    result.data.forEach(d => {
    const card = document.createElement("div");
    card.className = "item-card";
    card.innerHTML = `
                <h4>${d.name}</h4>
                <p><strong>Email:</strong> ${d.email}</p>
                <p><strong>Amount:</strong> $${d.amount}</p>
                <button onclick="openEditModal('donor', ${d.id}, '${d.name}', '${d.email}', '${d.amount}')">Edit</button>
                <button onclick="deleteDonor(${d.id})">Delete</button>
            `;
    container.appendChild(card);
});
} catch(err) { console.error("Error loading donors:", err); }
}

    document.getElementById("addDonorForm").addEventListener("submit", async e => {
    e.preventDefault();
    const don = { name: donName.value, email: donEmail.value, amount: donAmount.value };
    try {
    await fetchWithToken(`${BASE}/donors`, { method:"POST", body: JSON.stringify(don) });
    loadDonors();
    e.target.reset();
    alert("✅ Donor added successfully!");
} catch(err) { console.error("Error adding donor:", err); }
});

    async function deleteDonor(id) {
    if (!confirm("Are you sure you want to delete this donor?")) return;
    try {
    await fetchWithToken(`${BASE}/donors/${id}`, { method:"DELETE" });
    loadDonors();
} catch(err) { console.error("Error deleting donor:", err); }
}

    // -------------------- Users --------------------
    async function loadUsers() {
    try {
    const data = await fetchWithToken(`${BASE}/users`);
    const container = document.getElementById("userList");
    container.innerHTML = "";
    data.data.forEach(user => {
    const card = document.createElement("div");
    card.className = "list-card";
    card.innerHTML = `
                <p><strong>${user.username}</strong> (${user.role})</p>
                <p>${user.email}</p>
                <button onclick="deleteUser(${user.id})">Delete</button>
            `;
    container.appendChild(card);
});
} catch(err) { console.error("Error loading users:", err); }
}

    async function deleteUser(id) {
    if (!confirm("Are you sure you want to delete this user?")) return;
    try {
    await fetchWithToken(`${BASE}/users/${id}`, { method: "DELETE" });
    loadUsers();
} catch(err) { console.error("Error deleting user:", err); }
}

    // -------------------- Edit Modal --------------------
    function openEditModal(type, id, name, email, field1="", field2="") {
    const modal = document.getElementById("popupModal");
    const form = document.getElementById("updateForm");
    const title = document.getElementById("modalTitle");

    let html = `<input type="hidden" id="editId" value="${id}">`;

    if(type==="volunteer") {
    title.innerText="Edit Volunteer";
    html += `
            <label>Name</label><input type="text" id="editName" value="${name}" required>
            <label>Email</label><input type="email" id="editEmail" value="${email}" required>
            <label>Phone</label><input type="text" id="editField1" value="${field1}">
            <label>Skills</label><input type="text" id="editField2" value="${field2}">
            <button type="submit">Save</button>
        `;
}
    if(type==="donor") {
    title.innerText="Edit Donor";
    html += `
            <label>Name</label><input type="text" id="editName" value="${name}" required>
            <label>Email</label><input type="email" id="editEmail" value="${email}" required>
            <label>Amount</label><input type="number" id="editField" value="${field1}" required>
            <button type="submit">Save</button>
        `;
}

    form.innerHTML = html;
    form.onsubmit = async function(e){
    e.preventDefault();
    let updated={};
    if(type==="volunteer") {
    updated = {
    name: editName.value,
    email: editEmail.value,
    phone: editField1.value,
    skills: editField2.value
};
    await fetchWithToken(`${BASE}/volunteers/${id}`, { method:"PUT", body: JSON.stringify(updated) });
    alert("✅ Updated successfully!");
    loadVolunteers();
}
    if(type==="donor") {
    updated = {
    name: editName.value,
    email: editEmail.value,
    amount: editField.value
};
    await fetchWithToken(`${BASE}/donors/${id}`, { method:"PUT", body: JSON.stringify(updated) });
    alert("✅ Updated successfully!");
    loadDonors();

}
    closeModal();
};
    modal.style.display="block";
}

    function closeModal(){ document.getElementById("popupModal").style.display="none"; }

    // -------------------- Volunteer Search --------------------
    function filterVolunteers() {
        const input = document.getElementById("volunteerSearch").value.toLowerCase();
        document.querySelectorAll("#volunteerList .item-card").forEach(card => {
            const name = card.querySelector("h4").innerText.toLowerCase();
            const email = card.querySelector("p").innerText.toLowerCase();
            card.style.display = (name.includes(input) || email.includes(input)) ? "block" : "none";
        });
    }

    async function filterDonors() {
        const keyword = document.getElementById("donorSearch").value.trim();

        // Empty keyword නම් සියලු donors නැවත load කරන්න
        if (!keyword) {
            loadDonors();
            return;
        }

        try {
            const res = await fetchWithToken(`${BASE}/donors/search/${keyword}`);
            const data = res.data;

            const container = document.getElementById("donorList");
            container.innerHTML = "";

            data.forEach(d => {
                const card = document.createElement("div");
                card.className = "item-card";
                card.innerHTML = `
                <h4>${d.name}</h4>
                <p><strong>Email:</strong> ${d.email}</p>
                <p><strong>Amount:</strong> $${d.amount}</p>
                <button onclick="openEditModal('donor', ${d.id}, '${d.name}', '${d.email}', '${d.amount}')">Edit</button>
                <button onclick="deleteDonor(${d.id})">Delete</button>
            `;
                container.appendChild(card);
            });
        } catch(err) {
            console.error("Error searching donors:", err);
        }
    }


    // -------------------- Donor Search --------------------
    // function filterDonors() {
    //     const input = document.getElementById("donorSearch").value.toLowerCase();
    //     document.querySelectorAll("#donorList .item-card").forEach(card => {
    //         const name = card.querySelector("h4").innerText.toLowerCase();
    //         const email = card.querySelector("p").innerText.toLowerCase();
    //         const amount = card.querySelector("p:nth-of-type(2)")?.innerText.toLowerCase() || "";
    //         card.style.display = (name.includes(input) || email.includes(input) || amount.includes(input)) ? "block" : "none";
    //     });
    // }


    // -------------------- Logout --------------------
    document.getElementById("logoutLink").addEventListener("click", e=>{
    e.preventDefault();
    localStorage.clear();
    window.location.href="dashboard.html";
});

