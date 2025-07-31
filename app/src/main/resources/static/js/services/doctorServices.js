/*
  Doctor Service Module

  This module handles all API calls related to doctor data:
  - Fetching doctors
  - Filtering by criteria
  - Creating new doctors
  - Deleting doctors
*/

// === IMPORT BASE URL ===
import { API_BASE_URL } from "../config/config.js";   

// === DEFINE FULL DOCTOR API ENDPOINT ===
const DOCTOR_API = API_BASE_URL + '/doctor'

/*
  Function: getDoctors
  Purpose: Fetch the list of all doctors from the backend
*/
export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API); // GET request
    const data = await response.json();       // Parse JSON
    return data.doctors || [];                // Return doctor list
  } catch (error) {
    console.error("Error fetching doctors:", error);
    return [];                                // Return empty on error
  }
}

/*
  Function: deleteDoctor
  Purpose: Delete a doctor by ID, using the admin's token
  Params:
    - id (string): Doctor's ID
    - token (string): Admin's auth token
*/
export async function deleteDoctor(id, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${id}/${token}`, {
      method: "DELETE",
    });

    const result = await response.json();

    return {
      success: response.ok,
      message: result.message || "Doctor deleted",
    };
  } catch (error) {
    console.error("Error deleting doctor:", error);
    return {
      success: false,
      message: "Something went wrong",
    };
  }
}

/*
  Function: saveDoctor
  Purpose: Save a new doctor (admin role only)
  Params:
    - doctor (object): Doctor details (name, email, etc.)
    - token (string): Admin's auth token
*/
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${token}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(doctor),
    });

    const result = await response.json();

    return {
      success: response.ok,
      message: result.message || "Doctor saved",
    };
  } catch (error) {
    console.error("Error saving doctor:", error);
    return {
      success: false,
      message: "Something went wrong",
    };
  }
}

/*
  Function: filterDoctors
  Purpose: Get doctors based on filters (name, time, specialty)
  Params:
    - name (string): Doctor's name
    - time (string): Available time
    - specialty (string): Doctor's specialty
*/
export async function filterDoctors(name, time, specialty) {
  try {
    const response = await fetch(`${DOCTOR_API}/filter/${name}/${time}/${specialty}`);

    if (response.ok) {
      const data = await response.json();
      return data;
    } else {
      console.error("Filter request failed:", response.status);
      return { doctors: [] };
    }
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("Failed to filter doctors. Please try again.");
    return { doctors: [] };
  }
}
