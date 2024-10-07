document.addEventListener("DOMContentLoaded", function () {
    const sendBtn = document.getElementById("sendBtn");
    const cancelBtn = document.getElementById("cancelBtn");
    const messageInput = document.getElementById("officerMessage");
    const messageList = document.getElementById("messageList");
    const statusDropdown = document.getElementById("statusDropdown");
    const lastComplaintStatus = document.getElementById("lastComplaintStatus");

    sendBtn.addEventListener("click", function () {
        const message = messageInput.value.trim();
        if (message) {
            addMessage(message);
            messageInput.value = "";
        }
    });

    cancelBtn.addEventListener("click", function () {
        messageInput.value = "";
    });

    statusDropdown.addEventListener("change", function () {
        updateStatus(this.value);
    });

    function addMessage(message) {
        const messageElement = document.createElement("div");
        messageElement.classList.add("bg-white", "p-2", "rounded", "shadow-sm");
        messageElement.innerHTML = `
            <p class="text-sm text-gray-700">${message}</p>
            <p class="text-xs text-gray-500 mt-1">
              Time: ${new Date().toLocaleTimeString()}
              OfficerID: 12
            </p>
          `;
        messageList.appendChild(messageElement);
    }

    function updateStatus(status) {
        lastComplaintStatus.textContent = status;
        const pendingDot = document.getElementById("pendingDot");
        const inProgressDot = document.getElementById("inProgressDot");
        const resolvedDot = document.getElementById("resolvedDot");

        pendingDot.className =
            "w-2 h-2 rounded-full " +
            (status === "Pending" ? "bg-red-500" : "bg-gray-500");
        inProgressDot.className =
            "w-2 h-2 rounded-full " +
            (status === "In Progress" ? "bg-yellow-500" : "bg-gray-500");
        resolvedDot.className =
            "w-2 h-2 rounded-full " +
            (status === "Resolved" ? "bg-green-500" : "bg-gray-500");
    }
});

function toggleComplaint(element) {
    const complaintDetails = element
        .closest(".bg-blue-50")
        .querySelector(".complaint-details");
    complaintDetails.classList.toggle("hidden");
    const arrowImg = element.querySelector("img");
    arrowImg.classList.toggle("rotate-180");
}

function closeCard(button) {
    const card = button.closest(".bg-blue-50");
    card.style.display = "none";
}

function toggleMessageInput() {
    const messageInput = document.getElementById("messageInput");
    messageInput.classList.toggle("hidden");
}

document
    .getElementById("addMessageBtn")
    .addEventListener("click", toggleMessageInput);
document
    .getElementById("cancelBtn")
    .addEventListener("click", toggleMessageInput);
