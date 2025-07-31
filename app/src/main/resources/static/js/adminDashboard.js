/*
  Admin Dashboard Script for Managing Doctors

  Responsibilities:
  - Load and display doctor cards
  - Filter doctors based on user input
  - Add new doctors using a modal form
*/

// === IMPORTS ===
import { getDoctors, saveDoctor, filterDoctors } from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";
import { openModal, closeModal } from "./components/modals.js";

// === SELECT ELEMENTS ===
const contentDiv = document.getElementById("content");
const addDoctorBtn = document.getElementById("addDoctorBtn");
const searchInput = document.getElementById("searchInput");
const timeFilter = document.getElementById("timeFilter");
const specialtyFilter = document.getElementById("specialtyFilter");

// === ATTACH EVENT TO "ADD DOCTOR" BUTTON ===
addDoctorBtn?.addEventListener("click", () => {
  openModal("addDoctor");
});

// === ON PAGE LOAD, FETCH & DISPLAY DOCTOR CARDS ===
window.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();
});

// === FUNCTION: loadDoctorCards ===
// Purpose: Load all doctors and render them in the dashboard
async function loadDoctorCards() {
  try {
    const doctors = await getDoctors();
    renderDoctorCards(doctors);
  } catch (error) {
    console.error("Failed to load doctors:", error);
  }
}

// === EVENT LISTENERS FOR FILTERS ===
searchInput?.addEventListener("input", filterDoctorsOnChange);
timeFilter?.addEventListener("change", filterDoctorsOnChange);
specialtyFilter?.addEventListener("change", filterDoctorsOnChange);

// === FUNCTION: filterDoctorsOnChange ===
// Purpose: Handle input changes and filter doctors accordingly
async function filterDoctorsOnChange() {
  const name = searchInput?.value || null;
  const time = timeFilter?.value || null;
  const specialty = specialtyFilter?.value || null;

  try {
    const result = await filterDoctors(name, time, specialty);

    if (result.doctors && result.doctors.length > 0) {
      renderDoctorCards(result.doctors);
    } else {
      contentDiv.innerHTML = `<p class="no-record">No doctors found with the given filters.</p>`;
    }
  } catch (error) {
    console.error("Filter error:", error);
    alert("Failed to filter doctors. Please try again.");
  }
}

// === FUNCTION: renderDoctorCards ===
// Purpose: Render doctor card elements to the content area
function renderDoctorCards(doctors) {
  contentDiv.innerHTML = ""; // Clear old content
  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

// === FUNCTION: adminAddDoctor ===
// Purpose: Handle form submission to add a new doctor
export async function adminAddDoctor(event) {
  event.preventDefault();

  // Get form input values
  const name = document.getElementById("doctorName").value;
  const email = document.getElementById("doctorEmail").value;
  const phone = document.getElementById("doctorPhone").value;
  const password = document.getElementById("doctorPassword").value;
  const specialty = document.getElementById("doctorSpecialty").value;
  const timeSlots = document.getElementById("doctorTimes").value.split(",").map(s => s.trim());

  // Get token
  const token = localStorage.getItem("token");
  if (!token) {
    alert("You must be logged in as admin to perform this action.");
    return;
  }

  // Build doctor object
  const newDoctor = { name, email, phone, password, specialty, availableTimes: timeSlots };

  try {
    const response = await saveDoctor(newDoctor, token);

    if (response.success) {
      alert("Doctor added successfully.");
      closeModal("addDoctor");
      loadDoctorCards(); // Refresh list
    } else {
      alert(`Failed to add doctor: ${response.message}`);
    }
  } catch (error) {
    console.error("Add doctor failed:", error);
    alert("Something went wrong while adding the doctor.");
  }
}
