document.addEventListener("DOMContentLoaded", function () {
    const officerNameElement = document.getElementById("officerName");
    const officerIdElement = document.getElementById("officeId");
    const lastLoginElement = document.getElementById("lastLogin");
    const dateRegisteredElement = document.getElementById("dateRegistered");
    const complaintSection = document.getElementById("complaintSection");

    fetch("/officer", {
        method: 'GET',
        headers: {
            'Authorization': getToken(),
            'Content-Type': 'application/json',
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data && data.officer) {
            const officer = data.officer;
            officerNameElement.textContent = `${officer.firstName} ${officer.lastName}`;
            officerIdElement.textContent = `Officer ID: ${officer.id}`;
            lastLoginElement.textContent = `Last Login: ${new Date(officer.lastLogin).toLocaleString()}`;
            dateRegisteredElement.textContent = `Date Registered: ${new Date(officer.dateRegistered).toLocaleDateString()}`;
            fetchComplaints();
        }
    })
    .catch(error => {
        console.error("Error fetching officer details:", error);
    });

    fetchComplaints();
});

function getToken() {
    const cookies = document.cookie.split('; ');
    for (let cookie of cookies) {
        const [name, value] = cookie.split('=');
        if (name === 'token') {
            return decodeURIComponent(value);
        }
    }
    return null;
}

function fetchComplaints() {
    fetch("/complaints", {
        method: 'GET',
        headers: {
            'Authorization': getToken(),
            'Content-Type': 'application/json',
        }
    })
    .then(response => response.json())
    .then(complaints => {
        displayComplaints(complaints);
    })
    .catch(error => {
        console.error("Error fetching complaints:", error);
    });
}

function displayComplaints(complaints) {
    complaintSection.innerHTML = '';

    complaints.forEach(complaint => {
        const complaintCard = document.createElement("div");
        complaintCard.className = "bg-blue-50 p-6 rounded-lg shadow-sm mb-6 relative complaint-card";

        const status = complaint.status;

        complaintCard.innerHTML = `
            <div class="absolute top-8 right-28 flex space-x-10">
                <div class="flex flex-col items-center">
                    <div class="w-2 h-2 rounded-full" id="pendingDot_${complaint.id}"></div>
                    <p class="text-xs font-bold text-gray-500 mt-2 uppercase">Pending</p>
                </div>
                <div class="flex flex-col items-center">
                    <div class="w-2 h-2 rounded-full" id="inProgressDot_${complaint.id}"></div>
                    <p class="text-xs text-gray-500 mt-2 uppercase">In Progress</p>
                </div>
                <div class="flex flex-col items-center">
                    <div class="w-2 h-2 rounded-full" id="closedDot_${complaint.id}"></div>
                    <p class="text-xs text-gray-500 mt-2 uppercase">Resolved</p>
                </div>
            </div>
            <div class="absolute top-8 right-6 cursor-pointer toggle-arrow" onclick="toggleComplaint(this)">
                <img alt="Up Arrow" class="w-4 h-4" src="assets/chevron_down_solid_icon.svg" />
            </div>
            <h3 class="text-lg font-bold mb-4">${complaint.title}</h3>
            <div class="complaint-details hidden">
                <p class="text-sm text-gray-600 mb-4">${complaint.summary}</p>
                <h4 class="text-md font-semibold mb-2">Image Received</h4>
                <div class="mediaSection" id="mediaSection_${complaint.id}"></div>
                <div class="flex items-center space-x-2 mb-4"></div>
                <p class="text-xs text-gray-500">Filed on: ${new Date(complaint.dateFiled).toLocaleDateString()}</p>
                <p class="text-xs text-gray-500">Last Updated: ${new Date(complaint.lastUpdated).toLocaleDateString()}</p>
                <div class="separator"></div>
                <label class="block text-sm font-medium text-gray-700" for="statusDropdown_${complaint.id}">Update Status:</label>
                <select id="statusDropdown_${complaint.id}" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md" onchange="updateComplaintStatus(${complaint.id}, this.value)">
                    <option value="PENDING" ${complaint.status === 'PENDING' ? 'selected' : ''}>Pending</option>
                    <option value="IN_PROGRESS" ${complaint.status === 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                    <option value="CLOSED" ${complaint.status === 'CLOSED' ? 'selected' : ''}>Resolved</option>
                </select>
                <span id="statusMessage_${complaint.id}" class="text-sm text-gray-600"></span>
                <div class="messages-section">
                    <div class="flex justify-between items-center mb-2">
                        <p class="text-sm font-bold">Messages:</p>
                        <button class="px-2 py-1 mt-4 text-black rounded-md hover:bg-blue-200 transition duration-300" id="addMessageBtn_${complaint.id}">
                            <img alt="Add Message" class="w-4 h-4 ml-9" src="assets/message_regular_icon.svg" /> Add Message
                        </button>
                    </div>
                    <div class="space-y-2 mb-4" id="messageList_${complaint.id}"></div>
                    <div class="hidden" id="messageInput_${complaint.id}">
                        <label for="officerMessage_${complaint.id}"></label>
                        <textarea class="block w-full p-2 mb-3 border border-gray-300 rounded" id="officerMessage_${complaint.id}" placeholder="Type your message here" rows="3"></textarea>
                        <p class="text-xs text-gray-500 mt-2">Time: <span id="time_${complaint.id}">${new Date().toLocaleTimeString()}</span></p>
                        <div class="flex justify-end space-x-4">
                            <button class="px-4 py-2 bg-gray-400 text-white font-semibold rounded hover:bg-gray-500 transition duration-300" id="cancelBtn_${complaint.id}">Cancel</button>
                            <button class="px-4 py-2 bg-blue-900 text-white font-semibold rounded hover:bg-[#160e4e] transition duration-300" id="sendBtn_${complaint.id}">Send</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        complaintSection.appendChild(complaintCard);

        const statusDropdown = document.getElementById(`statusDropdown_${complaint.id}`);
        statusDropdown.addEventListener("change", function () {
            updateStatus(complaint.id, this.value);
        });
        updateStatus(complaint.id, complaint.status);

        const messageList = document.getElementById(`messageList_${complaint.id}`);
        if (messageList.length === 0) {
            const messageElement = document.createElement("div");
            messageElement.innerHTML = `<p class="text-sm text-gray-700">"No messages yet."</p>`;
        }

        complaint.messages.forEach(msg => {
            const messageElement = document.createElement("div");
            messageElement.classList.add("bg-white", "p-2", "rounded", "shadow-sm");
            messageElement.innerHTML = `
                <p class="text-sm text-gray-700">${msg.body}</p>
                <p class="text-xs text-gray-500 mt-1">Time: ${new Date(msg.dateCommented).toLocaleTimeString()}</p>
            `;
            messageList.appendChild(messageElement);
        });

        const addMessageBtn = document.getElementById(`addMessageBtn_${complaint.id}`);
        addMessageBtn.addEventListener("click", function () {
            const messageInput = document.getElementById(`messageInput_${complaint.id}`);
            messageInput.classList.toggle("hidden");
        });

        const sendBtn = document.getElementById(`sendBtn_${complaint.id}`);
        sendBtn.addEventListener("click", function () {
            const message = document.getElementById(`officerMessage_${complaint.id}`).value.trim();
            if (message) {
                            document.getElementById(`officerMessage_${complaint.id}`).value = "";
            }
            const status = document.getElementById(`statusDropdown_${complaint.id}`).value;
            const complaintId = complaint.id;
            updateComplaintStatus(complaintId, status);

            if (message) {
                const data = { body: message };
                const url = `/complaints/${complaint.id}/messages`;
                fetch(url, {
                    method: 'POST',
                    headers: {
                        'Authorization': getToken(),
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(data),
                })
                .then(response => response.json())
                .then(newMessage => {
                    const messageElement = document.createElement("div");
                    messageElement.classList.add("bg-white", "p-2", "rounded", "shadow-sm");
                    messageElement.innerHTML = `
                        <p class="text-sm text-gray-700">${newMessage.body}</p>
                        <p class="text-xs text-gray-500 mt-1">Time: ${new Date(newMessage.dateCommented).toLocaleTimeString()}</p>
                    `;
                    messageList.appendChild(messageElement);
                    messageInput.classList.add("hidden");
                })
                .catch(error => {
                    console.error("Error sending message:", error);
                });
            }
        });

        const cancelBtn = document.getElementById(`cancelBtn_${complaint.id}`);
        cancelBtn.addEventListener("click", function () {
            const messageInput = document.getElementById(`messageInput_${complaint.id}`);
            messageInput.classList.add("hidden");
        });
        displayMedia(complaint.mediaList, `mediaSection_${complaint.id}`);
    });

}

function updateComplaintStatus(complaintId, status) {
    const token = getToken();

    fetch(`/complaints/${complaintId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token
        },
        body: JSON.stringify({ status: status })
    })
    .then(response => response.json())
    .then(data => {
        const statusMessageElement = document.getElementById(`statusMessage_${complaintId}`);
        if (data.message) {
            statusMessageElement.textContent = data.message;
        } else {
            statusMessageElement.textContent = 'Complaint status updated successfully!';
        }
        statusMessageElement.classList.add('text-green-600');
    })
    .catch(error => {
        console.error('Error:', error);
        const statusMessageElement = document.getElementById(`statusMessage_${complaintId}`);
        statusMessageElement.textContent = 'An error occurred while updating the status.';
        statusMessageElement.classList.add('text-red-600');
    });
}

function updateStatus(complaintId, status) {
    const pendingDot = document.getElementById(`pendingDot_${complaintId}`);
    const inProgressDot = document.getElementById(`inProgressDot_${complaintId}`);
    const closedDot = document.getElementById(`closedDot_${complaintId}`);

    pendingDot.className = "w-2 h-2 rounded-full " + (status === "PENDING" ? "bg-red-500" : "bg-gray-500");
    inProgressDot.className = "w-2 h-2 rounded-full " + (status === "IN_PROGRESS" ? "bg-yellow-500" : "bg-gray-500");
    closedDot.className = "w-2 h-2 rounded-full " + (status === "CLOSED" ? "bg-green-500" : "bg-gray-500");
}

function addMessage(message, complaintId) {
    const messageList = document.getElementById(`messageList_${complaintId}`);
    const messageElement = document.createElement("div");
    messageElement.classList.add("bg-white", "p-2", "rounded", "shadow-sm");
    messageElement.innerHTML = `
        <p class="text-sm text-gray-700">${message}</p>
        <p class="text-xs text-gray-500 mt-1">Time: ${new Date().toLocaleTimeString()}</p>
    `;
    messageList.appendChild(messageElement);
}

function logout() {
    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    window.location.href = '/officers';
}

function toggleComplaint(element) {
    const complaintDetails = element
        .closest(".bg-blue-50")
        .querySelector(".complaint-details");
    complaintDetails.classList.toggle("hidden");
    const arrowImg = element.querySelector("img");
    arrowImg.classList.toggle("rotate-180");
}

function displayMedia(mediaList, mediaSectionId) {
    const mediaContainer = document.getElementById(mediaSectionId);

    if (!mediaContainer) {
        console.error('mediaContainer not found');
        return;
    }

    mediaContainer.innerHTML = '';
    console.log(mediaList.length);
    const mediaCard = document.createElement("div");
    mediaCard.className = "flex items-center space-x-2 mb-4";

    mediaList.forEach(media => {
        const mediaDiv = document.createElement("div");
        mediaDiv.className = "crop-area";

        if (media.mediaType === "IMAGE") {
            const imgElement = document.createElement('img');
            imgElement.src = `data:image/jpeg;base64,${media.data}`;
            imgElement.alt = `Image ID: ${media.id}`;
            imgElement.classList.add('w-full', 'h-full', 'object-cover');

            mediaDiv.appendChild(imgElement);
            mediaCard.appendChild(mediaDiv);
        } else {
            const unsupportedMessage = document.createElement('p');
            unsupportedMessage.textContent = 'Unsupported media type';
            unsupportedMessage.classList.add('text-red-500');
            mediaCard.appendChild(unsupportedMessage);
        }

        mediaContainer.appendChild(mediaCard);
    });
}