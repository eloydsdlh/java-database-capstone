/*
  Function to create and return a DOM element for a single doctor card
  This card displays doctor info and role-specific action buttons
*/

// Import necessary functions from external files
import { showBookingOverlay } from "./loggedPatient.js";          // Overlay for booking appointments
import { deleteDoctor } from "../services/doctorServices.js";     // API call to delete a doctor (admin only)
import { fetchPatientByToken } from "../services/patientServices.js"; // Used to fetch patient data for logged-in patients

export function createDoctorCard(doctor) {
  // 1. Create the main container for the doctor card
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  // 2. Get current role from localStorage
  const role = localStorage.getItem("userRole");

  // 3. Create a container for doctor's information
  const infoDiv = document.createElement("div");
  infoDiv.classList.add("doctor-info");

  // 4. Doctor's name
  const name = document.createElement("h3");
  name.textContent = doctor.name;

  // 5. Doctor's specialization
  const specialization = document.createElement("p");
  specialization.textContent = doctor.specialization;

  // 6. Doctor's email
  const email = document.createElement("p");
  email.textContent = doctor.email;

  // 7. Appointment time slots
  const timing = document.createElement("p");
  timing.textContent = `Available: ${doctor.timeFrom} - ${doctor.timeTo}`;

  // 8. Append all info to info container
  infoDiv.appendChild(name, specialization, email, timing);

  // 9. Create container for action buttons
  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("doctor-actions");

  // === ADMIN ROLE ===
  if (role === "admin") {
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.classList.add("delete-btn");

    // Add delete handler
    deleteBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please log in again.");
        return;
      }

      try {
        const success = await deleteDoctor(doctor.id, token);
        if (success) {
          alert("Doctor deleted successfully.");
          card.remove();
        } else {
          alert("Failed to delete doctor.");
        }
      } catch (err) {
        console.error(err);
        alert("Error deleting doctor.");
      }
    });

    actionsDiv.appendChild(deleteBtn);
  }

  // === NON-LOGGED-IN PATIENT ===
  else if (role === "patient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.className = "book-btn";
    bookBtn.addEventListener("click", () => {
      alert("Please log in to book an appointment.");
    });
    actionsDiv.appendChild(bookBtn);
  }

  // === LOGGED-IN PATIENT ===
  else if (role === "loggedPatient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.className = "book-btn";

    bookBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Session expired. Please log in again.");
        return;
      }

      try {
        const patient = await fetchPatientByToken(token);
        if (!patient) {
          alert("Unable to fetch patient info.");
          return;
        }

        // Show booking overlay with doctor and patient info
        showBookingOverlay(doctor, patient);
      } catch (error) {
        console.error(error);
        alert("Error fetching patient data.");
      }
    });

    actionsDiv.appendChild(bookBtn);
  }

  // 10. Append info and actions to card
  card.appendChild(infoDiv);
  card.appendChild(actionsDiv);

  // 11. Return the complete card element
  return card;
}
