const BASE = "http://localhost:8080/api";

/* -------------------- Page Navigation -------------------- */
function showPage(pageId, el) {
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    document.getElementById(pageId).classList.add('active');

    document.querySelectorAll('.sidebar a').forEach(a => a.classList.remove('active'));
    if (el) el.classList.add('active');

    document.getElementById("pageTitle").innerText =
        pageId.charAt(0).toUpperCase() + pageId.slice(1);

    if (pageId === "employees") loadEmployees();
    if (pageId === "volunteers") loadVolunteers();
    if (pageId === "donors") loadDonors();
    if (pageId === "users") loadUsers();   // ✅ meka add karanna
}

/* -------------------- Volunteers -------------------- */
async function loadVolunteers() {
    const res = await fetch(`${BASE}/volunteers`);
    const result = await res.json();
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
}
document.getElementById("addVolunteerForm").addEventListener("submit", async e => {
    e.preventDefault();
    const vol = { name: volName.value, email: volEmail.value, phone: volPhone.value, skills: volSkills.value };
    await fetch(`${BASE}/volunteers`, {
        method:"POST",
        headers:{"Content-Type":"application/json"},
        body:JSON.stringify(vol)
    });
    loadVolunteers();
    e.target.reset();
});
async function deleteVolunteer(id) {
    if (!confirm("Are you sure you want to delete this volunteer?")) return;
    await fetch(`${BASE}/volunteers/${id}`, {method:"DELETE"});
    loadVolunteers();
}

/* -------------------- Donors -------------------- */
async function loadDonors() {
    const res = await fetch(`${BASE}/donors`);
    const data = await res.json();
    const container = document.getElementById("donorList");
    container.innerHTML = "";
    data.data.forEach(d => {
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
}
document.getElementById("addDonorForm").addEventListener("submit", async e => {
    e.preventDefault();
    const don = { name: donName.value, email: donEmail.value, amount: donAmount.value };
    await fetch(`${BASE}/donors`, {
        method:"POST",
        headers:{"Content-Type":"application/json"},
        body:JSON.stringify(don)
    });
    loadDonors();
    e.target.reset();
});
async function deleteDonor(id) {
    if (!confirm("Are you sure you want to delete this donor?")) return;
    await fetch(`${BASE}/donors/${id}`, {method:"DELETE"});
    loadDonors();
}

/* -------------------- Users -------------------- */
function loadUsers() {
    fetch("http://localhost:8080/api/users")
        .then(res => res.json())
        .then(data => {
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
        });
}

function deleteUser(id) {
    if (confirm("Are you sure you want to delete this user?")) {
        fetch(`http://localhost:8080/api/users/${id}`, {
            method: "DELETE"
        })
            .then(res => res.json())
            .then(() => {
                loadUsers();
            });
    }
}

/* -------------------- Popup Modal for Edit -------------------- */
function openEditModal(type, id, name, email, field1="", field2="") {
    const modal = document.getElementById("popupModal");
    const form = document.getElementById("updateForm");
    const title = document.getElementById("modalTitle");

    let html = `<input type="hidden" id="editId" value="${id}">`;

    // if (type === "employee") {
    //     title.innerText = "Edit Employee";
    //     html += `
    //         <label>Name</label><input type="text" id="editName" value="${name}" required>
    //         <label>Email</label><input type="email" id="editEmail" value="${email}" required>
    //         <label>Position</label><input type="text" id="editField" value="${field1}" required>
    //         <button type="submit">Save</button>`;
    // }

    if (type === "volunteer") {
        title.innerText = "Edit Volunteer";
        html += `
            <label>Name</label><input type="text" id="editName" value="${name}" required>
            <label>Email</label><input type="email" id="editEmail" value="${email}" required>
            <label>Phone</label><input type="text" id="editField1" value="${field1}">
            <label>Skills</label><input type="text" id="editField2" value="${field2}">
            <button type="submit">Save</button>`;
    }

    if (type === "donor") {
        title.innerText = "Edit Donor";
        html += `
            <label>Name</label><input type="text" id="editName" value="${name}" required>
            <label>Email</label><input type="email" id="editEmail" value="${email}" required>
            <label>Amount</label><input type="number" id="editField" value="${field1}" required>
            <button type="submit">Save</button>`;
    }

    form.innerHTML = html;
    form.onsubmit = async function(e) {
        e.preventDefault();
        const id = document.getElementById("editId").value;
        let updated = {};
        //
        // if (type === "employee") {
        //     updated = {
        //         name: editName.value,
        //         email: editEmail.value,
        //         position: editField.value
        //     };
        //     await fetch(`${BASE}/employees/${id}`, {
        //         method:"PUT", headers:{"Content-Type":"application/json"}, body:JSON.stringify(updated)
        //     });
        //     loadEmployees();
        // }

        if (type === "volunteer") {
            updated = {
                name: editName.value,
                email: editEmail.value,
                phone: editField1.value,
                skills: editField2.value
            };
            await fetch(`${BASE}/volunteers/${id}`, {
                method:"PUT", headers:{"Content-Type":"application/json"}, body:JSON.stringify(updated)
            });
            loadVolunteers();
        }

        if (type === "donor") {
            updated = {
                name: editName.value,
                email: editEmail.value,
                amount: editField.value
            };
            await fetch(`${BASE}/donors/${id}`, {
                method:"PUT", headers:{"Content-Type":"application/json"}, body:JSON.stringify(updated)
            });
            loadDonors();
        }

        closeModal();
    };

    modal.style.display = "block";
}

function filterVolunteers() {
    const input = document.getElementById("volunteerSearch").value.toLowerCase();
    const cards = document.querySelectorAll("#volunteerList .item-card");

    cards.forEach(card => {
        const name = card.querySelector("h4").innerText.toLowerCase();
        const email = card.querySelector("p strong") ? card.querySelector("p").innerText.toLowerCase() : "";

        if (name.includes(input) || email.includes(input)) {
            card.style.display = "block";
        } else {
            card.style.display = "none";
        }
    });
}


function closeModal() {
    document.getElementById("popupModal").style.display = "none";
}

/* -------------------- Default -------------------- */

