function addVolunteer(event) {
    event.preventDefault();

    const volunteer = {
        name: document.getElementById("volunteerName").value,
        email: document.getElementById("volunteerEmail").value,
        phone: document.getElementById("volunteerPhone").value,
        skills: document.getElementById("volunteerSkills").value
    };

    fetch("http://localhost:8080/api/volunteers", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(volunteer)
    })
        .then(res => {
            if (res.ok) {
                alert("Volunteer added successfully ✅");
                document.getElementById("addVolunteerForm").reset();
            } else {
                alert("Failed to add volunteer ❌");
            }
        })
        .catch(err => console.error("Error:", err));
}
