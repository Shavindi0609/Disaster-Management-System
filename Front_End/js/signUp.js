document.addEventListener('DOMContentLoaded', () => {
    const signUpButton = document.getElementById('signUp');
    const signInButton = document.getElementById('signIn');
    const container = document.getElementById('container');

    if (signUpButton && signInButton && container) {
        signUpButton.addEventListener('click', () => {
            container.classList.add("right-panel-active");
            console.log("Sign Up button clicked - adding class");
        });

        signInButton.addEventListener('click', () => {
            container.classList.remove("right-panel-active");
            console.log("Sign In button clicked - removing class");
        });
    } else {
        console.error("Could not find one or more elements: #signUp, #signIn, #container");
    }
});

    document.addEventListener("DOMContentLoaded", function () {

        // =====================
        // Sign In Form Handler (Email instead of Username)
        // =====================
        const signInForm = document.querySelector(".sign-in-container form");

        signInForm.addEventListener("submit", async function (e) {
            e.preventDefault();

            // 👇 Use email instead of username
            const email = signInForm.querySelector("input[type='email']").value.trim();
            const password = signInForm.querySelector("input[type='password']").value;

            if (!email || !password) {
                alert("⚠️ Please enter email and password.");
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/auth/login", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ email, password }) // ✅ send email to backend
                });

                const result = await response.json();

                if (response.ok && result.data) {

                    const { accessToken, role, username, email } = result.data;

                    localStorage.setItem("accessToken", accessToken);
                    localStorage.setItem("role", role);
                    localStorage.setItem("username", username); // display name
                    localStorage.setItem("email", email);


                    alert("✅ Login successful!");

                    switch(role?.toUpperCase()) {
                        case "ADMIN":
                            window.location.href = "adminDashboard.html";
                            break;
                        case "USER":
                            window.location.href = "userDashboard.html";
                            break;
                        default:
                            window.location.href = "dashboard.html";
                    }


                } else {
                    alert(result.message || "❌ Invalid credentials.");
                }
            } catch (error) {
                console.error("Login error:", error);
                alert("🚨 Server error, please try again.");
            }
        });

    });

    // =====================
    // Sign Up Form Handler
    // =====================
    const signUpForm = document.querySelector(".sign-up-container form");
    signUpForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        // 👇 username use කරන්න
        const username = signUpForm.querySelector("input[placeholder='👤 Username']").value.trim();
        const email = signUpForm.querySelector("input[type='email']").value.trim();
        const password = signUpForm.querySelector("input[type='password']").value;

        if (!username || !email || !password) {
            alert("⚠️ Please fill all fields.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ username, email, password }) // ✅ username දාන්න
            });

            const result = await response.json();

            if (response.ok) {
                alert("🎉 Registration successful! Please login.");
                // Switch to Sign In view
                document.getElementById("signIn").click();
            } else {
                alert(result.message || "❌ Registration failed.");
            }
        } catch (error) {
            console.error("Signup error:", error);
            alert("🚨 Server error during registration.");
        }
    });




// =====================
// Forgot Password Modal Logic
// =====================
const modal = document.getElementById("forgotPasswordModal");
const forgotLink = document.getElementById("forgotPasswordLink");
const closeBtn = modal.querySelector(".close");

// Open modal with auto-fill email
forgotLink.addEventListener("click", (e) => {
    e.preventDefault();
    const loginEmail = document.getElementById("loginEmail").value.trim();
    if (loginEmail) {
        document.getElementById("resetEmail").value = loginEmail; // auto-fill login email
    }
    modal.style.display = "flex";
});

// Close modal
closeBtn.addEventListener("click", () => modal.style.display = "none");

// Close if click outside
window.addEventListener("click", (e) => {
    if (e.target === modal) {
        modal.style.display = "none";
    }
});

// Step 1: Send OTP
document.getElementById("sendOtpBtn").addEventListener("click", async () => {
    const email = document.getElementById("resetEmail").value.trim();
    if (!email) return alert("Please enter your email");

    try {
        const res = await fetch("http://localhost:8080/auth/password/send-otp", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email })
        });

        if (res.ok) {
            alert("✅ OTP sent to your email");
            document.getElementById("step1").style.display = "none";
            document.getElementById("step2").style.display = "block";
        } else {
            alert("❌ Failed to send OTP");
        }
    } catch (err) {
        alert("🚨 Server error");
    }
});

// Step 2: Verify OTP
document.getElementById("verifyOtpBtn").addEventListener("click", async () => {
    const email = document.getElementById("resetEmail").value.trim();
    const otp = document.getElementById("otpCode").value.trim();

    try {
        const res = await fetch("http://localhost:8080/auth/password/verify-otp", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, otp })
        });

        if (res.ok) {
            alert("✅ OTP verified");
            document.getElementById("step2").style.display = "none";
            document.getElementById("step3").style.display = "block";
        } else {
            alert("❌ Invalid OTP");
        }
    } catch (err) {
        alert("🚨 Server error");
    }
});

// Step 3: Reset Password
document.getElementById("resetPasswordBtn").addEventListener("click", async () => {
    const email = document.getElementById("resetEmail").value.trim();
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (newPassword !== confirmPassword) {
        return alert("⚠️ Passwords do not match");
    }

    try {
        const res = await fetch("http://localhost:8080/auth/password/reset", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, newPassword })
        });

        if (res.ok) {
            alert("✅ Password reset successful! Please login again.");
            modal.style.display = "none";
        } else {
            alert("❌ Failed to reset password");
        }
    } catch (err) {
        alert("🚨 Server error");
    }
});

// Volunteer Signup
const volunteerForm = document.getElementById("volunteerForm");

if (volunteerForm) {
    volunteerForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const name = volunteerForm.querySelector("input[name='name']").value.trim();
        const email = volunteerForm.querySelector("input[name='email']").value.trim();
        const password = volunteerForm.querySelector("input[name='password']").value;
        const phone = volunteerForm.querySelector("input[name='phone']").value.trim();
        const skills = volunteerForm.querySelector("input[name='skills']").value.trim();

        if (!name || !email || !password || !phone) {
            return alert("⚠️ කරුණාකර සියලු required fields පිරවන්න.");
        }

        try {
            const response = await fetch("http://localhost:8080/auth/volunteers", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name, email, password, phone, skills })
            });

            const result = await response.json();

            if (response.ok) {
                alert("🎉 Volunteer ලියාපදිංචිය සාර්ථකයි!");

                // Signup form hide කරන්න
                volunteerForm.parentElement.style.display = "none";

                // Volunteer login form show කරන්න
                const volunteerLoginFormContainer = document.querySelector(".form-volunteer-login");
                if (volunteerLoginFormContainer) volunteerLoginFormContainer.style.display = "block";
            } else {
                alert(result.message || "❌ Volunteer registration failed.");
            }
        } catch (err) {
            console.error("Volunteer signup error:", err);
            alert("🚨 Server error during volunteer registration.");
        }
    });
}


// Volunteer Login
const volunteerLoginForm = document.getElementById("volunteerLoginForm");

if (volunteerLoginForm) {
    volunteerLoginForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const email = volunteerLoginForm.querySelector("input[name='email']").value.trim();
        const password = volunteerLoginForm.querySelector("input[name='password']").value;

        if (!email || !password) {
            return alert("⚠️ Please enter email and password.");
        }

        try {
            const response = await fetch("http://localhost:8080/auth/volunteers/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password })
            });

            let result = {};
            try {
                result = await response.json(); // try parse
            } catch {
                // empty or non-json response
                result = { message: response.statusText || "Server error" };
            }

            if (response.ok) {
                const { accessToken, role, username, email } = result;

                localStorage.setItem("accessToken", accessToken);
                localStorage.setItem("role", role);
                localStorage.setItem("username", username);
                localStorage.setItem("email", email);

                alert("✅ Volunteer login successful!");
                window.location.href = "volunteerDashboard.html";
            } else {
                alert(result.message || "❌ Invalid credentials.");
            }
        } catch (err) {
            console.error("Volunteer login error:", err);
            alert("🚨 Server error during volunteer login.");
        }

    });
}


function showForm(type) {
    if(type === "user"){
        document.querySelector(".form-user").style.display = "block";
        document.querySelector(".form-volunteer").style.display = "none";
        document.querySelector(".form-volunteer-login").style.display = "none";
    } else {
        document.querySelector(".form-user").style.display = "none";
        document.querySelector(".form-volunteer").style.display = "block";
        document.querySelector(".form-volunteer-login").style.display = "none"; // Login hidden initially
    }

    document.querySelectorAll(".tab-btn").forEach(btn => btn.classList.remove("active"));
    if(type === "user") document.querySelector(".tab-btn:nth-child(1)").classList.add("active");
    else document.querySelector(".tab-btn:nth-child(2)").classList.add("active");
}
