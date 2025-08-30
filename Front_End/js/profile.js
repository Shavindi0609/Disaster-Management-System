// Global variables
let token = localStorage.getItem("accessToken");
let userId;

document.addEventListener("DOMContentLoaded", async () => {
    if (!token) {
        alert("⚠️ Please login first.");
        window.location.href = "signIn.html";
        return;
    }

    // Elements
    const userNameEl = document.getElementById("user-name");
    const userEmailEl = document.getElementById("user-email");
    const userJoinedEl = document.getElementById("user-joined");
    const editUsername = document.getElementById("editUsername");
    const editEmail = document.getElementById("editEmail");
    const editPassword = document.getElementById("editPassword");
    const editProfileForm = document.getElementById("editProfileForm");

    try {
        // Fetch user profile
        const response = await fetch("http://localhost:8080/auth/users/me", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        });

        const result = await response.json();

        if (response.ok && result.data) {
            const user = result.data;
            userId = user.id;

            // Update profile card
            userNameEl.textContent = user.username;
            userEmailEl.textContent = user.email;
            userJoinedEl.textContent = user.createdAt?.split("T")[0] || "N/A";

            // Prefill edit form
            editUsername.value = user.username;
            editEmail.value = user.email;
            editPassword.value = "";
        } else {
            alert(result.message || "❌ Failed to load profile.");
        }

    } catch (err) {
        console.error("Profile fetch error:", err);
        alert("🚨 Could not load profile.");
    }

    // Handle form submit
    editProfileForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const data = {
            username: editUsername.value.trim(),
            email: editEmail.value.trim()
        };

        if (editPassword.value.trim() !== "") {
            data.password = editPassword.value.trim();
        }

        try {
            const res = await fetch(`http://localhost:8080/auth/users/${userId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(data)
            });

            const updatedUser = await res.json();

            if (res.ok) {
                // If backend returns APIResponse { status, message, data }
                const user = updatedUser.data;

                userNameEl.textContent = user.username;
                userEmailEl.textContent = user.email;

                // Close modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('editProfileModal'));
                modal.hide();

                // Clear password field
                editPassword.value = "";

                alert("✅ Profile updated successfully!");
            } else {
                alert(updatedUser.message || "❌ Failed to update profile.");
            }


        } catch (err) {
            console.error("Profile update error:", err);
            alert("🚨 Error updating profile.");
        }
    });
});
