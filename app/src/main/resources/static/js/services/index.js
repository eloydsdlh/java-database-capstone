/*
  Login Logic Script for Admin and Doctor
  Handles UI modal logic and backend authentication via fetch API
*/

// === IMPORTS ===
import { openModal } from "../components/modals.js";         // Function to open modal windows
import { API_BASE_URL } from "../config/config.js";           // Base URL for API
import { selectRole } from "../render.js";
// === API ENDPOINTS ===
const ADMIN_API = API_BASE_URL + '/admin';
const DOCTOR_API = API_BASE_URL + '/doctor/login'

// === DOM READY HANDLER ===
window.onload = function () {
  // 1. Get login buttons from the DOM
  const adminBtn = document.getElementById("adminLogin");
  const doctorBtn = document.getElementById("doctorLogin");

  // 2. If Admin login button exists, attach event to open modal
  if (adminBtn) {
    adminBtn.addEventListener("click", () => {
      openModal("adminLogin");
    });
  }

  // 3. If Doctor login button exists, attach event to open modal
  if (doctorBtn) {
    doctorBtn.addEventListener("click", () => {
      openModal("doctorLogin");
    });
  }
};

// === ADMIN LOGIN HANDLER ===
window.adminLoginHandler = async () => {
  try {
    // Step 1: Get input values
    const username = document.getElementById("adminUsername").value;
    const password = document.getElementById("adminPassword").value;

    // Step 2: Create login object
    const admin = { username, password };

    // Step 3: Send POST request to admin login API
    const response = await fetch(ADMIN_API, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(admin),
    });

    // Step 4: Handle successful login
    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("userRole", "admin");
      selectRole("admin");
    } else {
      // Step 5: Handle failed login
      alert("Invalid admin credentials. Please try again.");
    }
  } catch (error) {
    // Step 6: Catch errors
    console.error("Admin login error:", error);
    alert("An error occurred. Please try again later.");
  }
};

// === DOCTOR LOGIN HANDLER ===
window.doctorLoginHandler = async () => {
  try {
    // Step 1: Get input values
    const email = document.getElementById("doctorEmail").value;
    const password = document.getElementById("doctorPassword").value;

    // Step 2: Create doctor login object
    const doctor = { email, password };

    // Step 3: Send POST request to doctor login API
    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(doctor),
    });

    // Step 4: Handle successful login
    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("userRole", "doctor");
      selectRole("doctor");
    } else {
      // Step 5: Handle failed login
      alert("Invalid doctor credentials. Please try again.");
    }
  } catch (error) {
    // Step 6: Catch errors
    console.error("Doctor login error:", error);
    alert("An error occurred. Please try again later.");
  }
};
