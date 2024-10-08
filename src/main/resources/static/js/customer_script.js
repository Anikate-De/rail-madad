let complaintStatus = "Pending"; // Default status

function toggleComplaintForm() {
    const addComplaintBtn = document.getElementById("addComplaintBtn");
    const complaintsSection = document.getElementById("complaintsSection");
    const existingForm = document.querySelector(".new-complaint-form");

    // Remove existing form if any
    if (existingForm) {
        existingForm.remove();
        return;
    }

    // Create the new complaint form
    const newComplaintForm = document.createElement("div");
    newComplaintForm.classList.add(
        "bg-red-50",
        "p-6",
        "rounded-lg",
        "shadow-sm",
        "mb-6",
        "relative",
        "new-complaint-form"
    );
    newComplaintForm.innerHTML = `
                <div class="absolute top-8 right-28 flex space-x-10">
                    <div class="flex flex-col items-center">
                        <div class="w-2 h-2 bg-red-500 rounded-full"></div>
                        <p class="text-xs font-bold text-gray-500 mt-2 uppercase">Pending</p>
                    </div>
                    <div class="flex flex-col items-center">
                        <div class="w-2 h-2 bg-gray-500 rounded-full"></div>
                        <p class="text-xs text-gray-500 mt-2 uppercase">In Progress</p>
                    </div>
                    <div class="flex flex-col items-center">
                        <div class="w-2 h-2 bg-gray-500 rounded-full"></div>
                        <p class="text-xs text-gray-500 mt-2 uppercase">Resolved</p>
                    </div>
                </div>
                <div class="absolute top-8 right-6 cursor-pointer toggle-arrow">
                    <img src="assets/chevron_down_solid_icon.svg" alt="Down Arrow" class="w-4 h-4">
                </div>
                <h3 class="text-lg font-bold mb-2">Add New Complaint</h3>

                <label class="block text-sm font-medium text-gray-700 mb-2">Title</label>
                <input
                    type="text"
                    id="complaintTitle"
                    class="block w-full p-2 mb-3 border border-gray-300 rounded"
                    placeholder="Enter the title of your complaint"
                />
                <p class="text-xs text-gray-500 mt-1">
                  Date Filed: <span>${new Date().toLocaleDateString()}</span>
                </p>
                <p class="text-xs text-gray-500">Last Updated: <span>${
        //get time now
        new Date().toLocaleDateString()
    }</span></p>
                <label class="block text-sm font-medium text-gray-700 mb-2">Summary</label>
                <textarea
                    id="complaintSummary"
                    rows="3"
                    class="block w-full p-2 mb-3 border border-gray-300 rounded"
                    placeholder="Enter a brief description of the issue"
                ></textarea>
                <h4 class="text-md font-semibold mb-2">Add Images (up to 3)</h4>
                <div class="flex items-center space-x-2 mb-4">
                    <div class="crop-area" id="cropArea1" onclick="document.getElementById('imageUpload1').click()">
                        <span>+</span>
                    </div>
                    <input type="file" id="imageUpload1" accept="image/*" class="hidden" multiple onchange="handleImageUpload(event, 1)" />
                    <div class="crop-area" id="cropArea2" onclick="document.getElementById('imageUpload2').click()">
                        <span>+</span>
                    </div>
                    <input type="file" id="imageUpload2" accept="image/*" class="hidden" multiple onchange="handleImageUpload(event, 2)" />
                    <div class="crop-area" id="cropArea3" onclick="document.getElementById('imageUpload3').click()">
                        <span>+</span>
                    </div>
                    <input type="file" id="imageUpload3" accept="image/*" class="hidden" multiple onchange="handleImageUpload(event, 3)" />
                </div>
                <div class="flex justify-end space-x-4">
                    <button id="cancelBtn" class="px-4 py-2 bg-gray-400 text-white font-semibold py-2 px-4 rounded hover:bg-gray-500 transition duration-300">Cancel</button>
                    <button id="submitBtn" class="px-4 py-2 bg-[#7A072A] text-white font-semibold py-2 px-4 rounded hover:bg-[#6b0421] transition duration-300">Submit</button>
                </div>
                <div class="separator"></div>
                <div class="messages-section">
              <p class="text-sm font-bold">Messages from Officer:</p>
              <p class="text-xs text-gray-500">
                Any messages in the future that you'll receive from the officer
                will appear here.
              </p>
              <p class="text-xs text-gray-500 mt-2">
                Time: <span id="time">12:15:03</span>

                OfficerID:
                <span id="officerID">001122</span>
              </p>
            </div>

            `;

    complaintsSection.insertBefore(
        newComplaintForm,
        addComplaintBtn.parentElement
    );

    // Cancel button event
    newComplaintForm
        .querySelector("#cancelBtn")
        .addEventListener("click", function () {
            newComplaintForm.remove();
        });

    // Submit button event
    newComplaintForm
        .querySelector("#submitBtn")
        .addEventListener("click", function () {
            const title = document.getElementById("complaintTitle").value;
            const summary = document.getElementById("complaintSummary").value;

            if (title && summary) {
                const newComplaintItem = document.createElement("div");
                newComplaintItem.classList.add(
                    "complaint-item",
                    "bg-red-50",
                    "p-6",
                    "rounded-lg",
                    "shadow-sm",
                    "mb-6",
                    "relative"
                );
                newComplaintItem.innerHTML = `
                        <div class="absolute top-8 right-28 flex space-x-10">
                            <div class="flex flex-col items-center">
                                <div class="w-2 h-2 bg-red-500 rounded-full"></div>
                                <p class="text-xs font-bold text-gray-500 mt-2 uppercase">Pending</p>
                            </div>
                            <div class="flex flex-col items-center">
                                <div class="w-2 h-2 bg-gray-500 rounded-full"></div>
                                <p class="text-xs text-gray-500 mt-2 uppercase">In Progress</p>
                            </div>
                            <div class="flex flex-col items-center">
                                <div class="w-2 h-2 bg-gray-500 rounded-full"></div>
                                <p class="text-xs text-gray-500 mt-2 uppercase">Resolved</p>
                            </div>
                        </div>
                        <div class="absolute top-8 right-6 cursor-pointer toggle-arrow" onclick="toggleComplaint(this)">
                            <img src="assets/chevron_down_solid_icon.svg" alt="Down Arrow" class="w-4 h-4">
                        </div>
                        <h3 class="text-lg font-bold">${title}</h3>
                        <div class="hidden complaint-details">
                            <p class="text-xs text-gray-500 mt-1">
                            Date Filed: <span id="dateFiled">12-09-2024</span>
                            </p>
                            <p class="text-xs text-gray-500">
                            Last Updated: <span id="lastUpdated">12-09-2024</span>
                            </p>
                            <p class="text-sm text-gray-600 mt-2">${summary}</p>
                            <div class="flex mt-2" id="imageContainerMock"></div>
                            <div class="separator"></div>

                            <div class="messages-section">
                            <p class="text-sm font-bold">Messages from Officer:</p>
                            <p class="text-xs text-gray-500">
                                Any messages in the future that you'll receive from the officer
                                will appear here.
                            </p>
                            </div>
                        </div>
                        

                    `;
                const imageContainerMock = newComplaintItem.querySelector(
                    "#imageContainerMock"
                );
                for (let i = 1; i <= 3; i++) {
                    const cropArea = document.getElementById(`cropArea${i}`);
                    const imgElement = cropArea.querySelector("img");
                    if (imgElement) {
                        const imgClone = imgElement.cloneNode();
                        imgClone.classList.add(
                            "w-24",
                            "h-24",
                            "object-cover",
                            "rounded",
                            "mr-2"
                        );
                        imageContainerMock.appendChild(imgClone);
                    }
                }

                complaintsSection.insertBefore(
                    newComplaintItem,
                    addComplaintBtn.parentElement
                );
                newComplaintForm.remove();
            } else {
                alert("Please fill out the title and summary!");
            }
        });
}

function toggleComplaint(element) {
    const complaintDetails = element
        .closest(".complaint-item")
        .querySelector(".complaint-details");
    complaintDetails.classList.toggle("hidden");
    const arrowImg = element.querySelector("img");
    arrowImg.classList.toggle("rotate-180");
}

function handleImageUpload(event, index) {
    const files = event.target.files;
    const cropArea = document.getElementById(`cropArea${index}`);

    // Clear previous images
    cropArea.innerHTML = "";

    // Allow only 1 image per crop area
    if (files.length > 0) {
        const file = files[0];
        const reader = new FileReader();

        reader.onload = function (e) {
            const imgElement = document.createElement("img");
            imgElement.src = e.target.result;
            cropArea.appendChild(imgElement);
        };

        reader.readAsDataURL(file);
    }
}
function getTokenFromCookies() {
    const token = document.cookie
    .split('; ')
    .find(row => row.startsWith('cust_token='))
    ?.split('=')[1];

    return token;
}

async function fetchCustomerInfo() {
    const token = getTokenFromCookies();
    if (!token) {
        console.error('No token found');
        return;
    }

    console.log('Token:', token);
    try {
        const response = await fetch('/customers/info', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: token
        });

        console.log('Response Status:', response.status);
        if (!response.ok) {
            const errorText = await response.text()
            console.error('Error response:', errorText);
            throw new Error('Network response was not ok');
        }

        const customerInfo = await response.json();
        updateCustomerDashboard(customerInfo);
    } catch (error) {
        console.error('Error fetching customer info:', error);
    }
}

function updateCustomerDashboard(customerInfo) {
    document.querySelector('h2').textContent = `${customerInfo.firstName} ${customerInfo.lastName}`;
    document.querySelector('p.text-md').textContent = `+91 ${customerInfo.phoneNumber}`;
    document.querySelector('p.text-xs:last-of-type').textContent = `Last Login: ${new Date(customerInfo.lastLogin).toLocaleString()}`;
    document.querySelector('p.text-xs:nth-of-type(2)').textContent = `Date Registered: ${new Date(customerInfo.dateRegistered).toLocaleString()}`;
}

document.addEventListener('DOMContentLoaded', () => {
    fetchCustomerInfo();
});

