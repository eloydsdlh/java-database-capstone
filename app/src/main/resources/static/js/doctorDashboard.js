/*
  Appointment Management Script

  Responsibilities:
  - Fetch appointments from backend
  - Filter by date and patient name
  - Render appointments in table format
*/

// === IMPORTS ===
import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";
import { renderContent } from "./render.js";

// === DOM ELEMENTS ===
const tableBody = document.getElementById("patient-table-body");
const searchInput = document.getElementById("searchInput");
const todayBtn = document.getElementById("todayBtn");
const datePicker = document.getElementById("datePicker");

// === STATE VARIABLES ===
let selectedDate = new Date().toISOString().split("T")[0]; // 'YYYY-MM-DD'
let token = localStorage.getItem("token");
let patientName = null;

// === EVENT LISTENER: SEARCH INPUT ===
searchInput?.addEventListener("input", () => {
  const value = searchInput.value.trim();
  patientName = value !== "" ? value : null;
  loadAppointments();
});

// === EVENT LISTENER: "TODAY" BUTTON ===
todayBtn?.addEventListener("click", () => {
  selectedDate = new Date().toISOString().split("T")[0];
  if (datePicker) datePicker.value = selectedDate;
  loadAppointments();
});

// === EVENT LISTENER: DATE PICKER ===
datePicker?.addEventListener("change", () => {
  selectedDate = datePicker.value;
  loadAppointments();
});

// === FUNCTION: loadAppointments ===
// Purpose: Fetch and display appointments in the table
async function loadAppointments() {
  try {
    const response = await getAllAppointments(selectedDate, patientName, token);
    tableBody.innerHTML = ""; // Clear previous rows

    if (!response.appointments || response.appointments.length === 0) {
      const noDataRow = `<tr><td colspan="5">No Appointments found for this date.</td></tr>`;
      tableBody.innerHTML = noDataRow;
      return;
    }

    response.appointments.forEach((appointment) => {
      const patient = {
        id: appointment.id,
        name: appointment.patientName,
        phone: appointment.patientPhone,
        email: appointment.patientEmail,
        time: appointment.time,
        doctor: appointment.doctorName
      };

      const row = createPatientRow(patient);
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Failed to load appointments:", error);
    tableBody.innerHTML = `<tr><td colspan="5">Error loading appointments. Try again later.</td></tr>`;
  }
}

// === INITIALIZATION ===
window.addEventListener("DOMContentLoaded", () => {
  renderContent(); // Renders layout if needed
  loadAppointments(); // Load today's appointments
});
