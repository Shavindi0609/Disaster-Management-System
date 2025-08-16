function openReportForm() {
    document.getElementById("report-form").classList.remove("d-none");
    window.scrollTo(0, document.body.scrollHeight);
}

function submitReport(event) {
    event.preventDefault();

    const disasterType = document.getElementById("disaster-type").value;
    const location = document.getElementById("location").value;
    const description = document.getElementById("description").value;

    alert(`Report Submitted ✅\nDisaster: ${disasterType}\nLocation: ${location}\nDescription: ${description}`);

    // Reset Form
    document.getElementById("disaster-type").value = "";
    document.getElementById("location").value = "";
    document.getElementById("description").value = "";

    // Increase report count
    let count = document.getElementById("report-count");
    count.innerText = parseInt(count.innerText) + 1;
}
