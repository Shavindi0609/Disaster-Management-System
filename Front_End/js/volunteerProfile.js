let token = localStorage.getItem("accessToken");
let volunteerId;

document.addEventListener("DOMContentLoaded", async () => {
    if (!token) {
        alert("⚠️ Please login first.");
        window.location.href = "signIn.html";
        return;
    }

    const nameEl = document.getElementById("vol-name");
    const emailEl = document.getElementById("vol-email");
    const phoneEl = document.getElementById("vol-phone");
    const skillsEl = document.getElementById("vol-skills");
    const statusEl = document.getElementById("vol-status");

    const editName = document.getElementById("editVolName");
    const editEmail = document.getElementById("editVolEmail");
    const editPhone = document.getElementById("editVolPhone");
    const editSkills = document.getElementById("editVolSkills");
    const editPassword = document.getElementById("editVolPassword");
    const editForm = document.getElementById("editVolunteerForm");

    try {
        const res = await fetch("http://localhost:8080/api/volunteers/me", {
            headers: { "Authorization": `Bearer ${token}` }
        });
        const data = await res.json();
        if (res.ok && data.data) {
            const vol = data.data;
            volunteerId = vol.id;

            nameEl.textContent = vol.name;
            emailEl.textContent = vol.email;
            phoneEl.textContent = vol.phone;
            skillsEl.textContent = vol.skills || "-";
            statusEl.textContent = vol.active ? "Active" : "Inactive";

            // Prefill edit form
            editName.value = vol.name;
            editEmail.value = vol.email;
            editPhone.value = vol.phone;
            editSkills.value = vol.skills || "";
            editPassword.value = "";
        } else alert(data.message || "Failed to load profile.");
    } catch (err) { console.error(err); alert("Error loading profile."); }

    // Edit form submit
    editForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const payload = {
            name: editName.value.trim(),
            email: editEmail.value.trim(),
            phone: editPhone.value.trim(),
            skills: editSkills.value.trim()
        };
        if (editPassword.value.trim() !== "") payload.password = editPassword.value.trim();

        try {
            const res = await fetch(`http://localhost:8080/api/volunteers/${volunteerId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(payload)
            });
            const updated = await res.json();
            if (res.ok) {
                nameEl.textContent = updated.data.name;
                emailEl.textContent = updated.data.email;
                phoneEl.textContent = updated.data.phone;
                skillsEl.textContent = updated.data.skills || "-";

                const modal = bootstrap.Modal.getInstance(document.getElementById('editVolunteerModal'));
                modal.hide();
                editPassword.value = "";

                alert("✅ Profile updated successfully!");
            } else alert(updated.message || "Failed to update profile.");
        } catch (err) { console.error(err); alert("Error updating profile."); }
    });

    // Delete account
    document.getElementById("deleteVolunteerBtn").addEventListener("click", async () => {
        if (!confirm("🛑 Are you sure you want to delete your account?")) return;
        try {
            const res = await fetch(`http://localhost:8080/api/volunteers/${volunteerId}`, {
                method: "DELETE",
                headers: { "Authorization": `Bearer ${token}` }
            });
            const data = await res.json();
            if (res.ok) {
                alert("✅ Account deleted successfully!");
                localStorage.removeItem("accessToken");
                window.location.href = "signIn.html";
            } else alert(data.message || "Failed to delete account.");
        } catch (err) { console.error(err); alert("Error deleting account."); }
    });
});
