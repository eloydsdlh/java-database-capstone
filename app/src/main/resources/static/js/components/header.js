/*
  This file dynamically renders the header section for Hospital CMS pages,
  adapting based on the user's session status and role.
*/

// 1. Define the renderHeader Function
function renderHeader() {
  // 2. Select the Header Div where content will be injected
  const headerDiv = document.getElementById("header");

  // 3. Check if the current page is the root ("/")
  if (window.location.pathname.endsWith("/")) {
    localStorage.removeItem("userRole"); // Clear session role if root
    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>`;
    return; // Exit early, no user-specific header
  }

  // 4. Retrieve user session data (role and token)
  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");

  // 5. Initialize header content with base layout (logo + nav start)
  let headerContent = `<header class="header">
    <div class="logo-section">
      <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
      <span class="logo-title">Hospital CMS</span>
    </div>
    <nav>`;

  // 6. Handle invalid or expired sessions
  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/"; // Redirect to home
    return;
  }

  // 7. Add role-specific content to the header
  if (role === "admin") {
    headerContent += `
      <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
      <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "doctor") {
    headerContent += `
      <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
      <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "patient") {
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>`;
  } else if (role === "loggedPatient") {
    headerContent += `
      <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
      <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
      <a href="#" onclick="logoutPatient()">Logout</a>`;
  }

  // 9. Close the nav and header tags
  headerContent += `</nav></header>`;

  // 10. Render the final header into the DOM
  headerDiv.innerHTML = headerContent;

  // 11. Attach event listeners to header buttons (e.g., login)
  attachHeaderButtonListeners();
}

/* 13. Helper: attachHeaderButtonListeners()
   - Attach actions to patient login/signup buttons when present */
function attachHeaderButtonListeners() {
  const loginBtn = document.getElementById("patientLogin");
  const signupBtn = document.getElementById("patientSignup");

  if (loginBtn) {
    loginBtn.addEventListener("click", () => openModal("patientLogin"));
  }

  if (signupBtn) {
    signupBtn.addEventListener("click", () => openModal("patientSignup"));
  }
}

/* 14. Helper: logout()
   - Clears all user session data and returns to root page */
function logout() {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/";
}

/* 15. Helper: logoutPatient()
   - Logs out a patient and redirects to patient dashboard */
function logoutPatient() {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/";
}

// 16. Trigger initial header rendering
renderHeader();
