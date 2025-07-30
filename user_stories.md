# User Story Template

## Admin User Stories

### User Story 1

**Title:**  
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**  
1. The login page must accept a valid username and password.
2. Invalid credentials should trigger an appropriate error message.  
3. After successful login, the admin should be redirected to the admin dashboard.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Passwords must be encrypted and securely stored.
- Consider account lockout after multiple failed login attempts.

---

### User Story 2

**Title:**  
_As an admin, I want to log out of the portal, so that I can protect system access when I am not using it._

**Acceptance Criteria:**  
1. There is a clearly visible logout button on every page.
2. Clicking the logout button ends the admin session and redirects to the login page.  
3. Any attempt to access restricted pages after logout should redirect to the login screen.

**Priority:** High  
**Story Points:** 1  
**Notes:**  
- Session timeout should also log out the user automatically after inactivity.

---

### User Story 3

**Title:**  
_As an admin, I want to add doctors to the portal, so that they can access and use the system._

**Acceptance Criteria:**  
1. Admin can access a form to input new doctor details (e.g., name, email, specialty).  
2. On form submission, the doctor profile is saved and displayed in the doctor list.  
3. A confirmation message is shown upon successful addition.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Validate mandatory fields before submission.  
- Email address must be unique.

---

### User Story 4

**Title:**  
_As an admin, I want to delete a doctor's profile from the portal, so that I can manage active users effectively._

**Acceptance Criteria:**  
1. Admin can view a list of doctors with delete options next to each entry.  
2. Clicking delete prompts a confirmation dialog.  
3. Upon confirmation, the doctor profile is removed from the system and no longer visible.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Ensure deletion is soft delete or confirm implications (e.g., orphaned data).  
- Consider audit logging of deletion events.

---

### User Story 5

**Title:**  
_As an admin, I want to run a stored procedure in the MySQL CLI to get the number of appointments per month, so that I can track usage statistics._

**Acceptance Criteria:**  
1. A stored procedure exists that aggregates appointments per month.  
2. Admin is provided with the exact command or instructions to run it in MySQL CLI.  
3. Output includes month, year, and appointment count.

**Priority:** Medium  
**Story Points:** 3  
**Notes:**  
- Procedure should handle edge cases like no appointments or multiple time zones.  
- Consider converting this into a UI-based report in the future.

## Patient User Stories

### User Story 6

**Title:**  
_As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering._

**Acceptance Criteria:**  
1. The homepage displays a public list of doctors with basic details (e.g., name, specialty).  
2. Patients can filter or search by specialty or name.  
3. No login is required to access this list.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Do not expose sensitive or contact information in the public view.  
- Consider including a “Register to Book” CTA for conversion.

---

### User Story 7

**Title:**  
_As a patient, I want to sign up using my email and password, so that I can book appointments._

**Acceptance Criteria:**  
1. A registration form is available with email, password, and optional profile info.  
2. Form validation ensures required fields and email uniqueness.  
3. Upon successful sign-up, the user is redirected to the dashboard.

**Priority:** High  
**Story Points:** 2  
**Notes:**  
- Send email verification for account activation.  
- Store passwords securely (hashed and salted).

---

### User Story 8

**Title:**  
_As a patient, I want to log into the portal, so that I can manage my bookings._

**Acceptance Criteria:**  
1. The login page accepts registered email and password.  
2. Invalid login attempts display clear error messages.  
3. Successful login redirects to the patient dashboard.

**Priority:** High  
**Story Points:** 1  
**Notes:**  
- Consider “Remember Me” and “Forgot Password” options.

---

### User Story 9

**Title:**  
_As a patient, I want to log out of the portal, so that I can secure my account._

**Acceptance Criteria:**  
1. A logout option is visible in the patient dashboard.  
2. Clicking logout ends the session and redirects to the login/home page.  
3. Protected routes should not be accessible after logout.

**Priority:** High  
**Story Points:** 1  
**Notes:**  
- Session timeout logic should also apply for added security.

---

### User Story 10

**Title:**  
_As a patient, I want to log in and book an hour-long appointment with a doctor, so that I can get a consultation._

**Acceptance Criteria:**  
1. Patients can browse available time slots after logging in.  
2. Selecting a slot reserves a one-hour appointment with a chosen doctor.  
3. A booking confirmation message is shown and added to upcoming appointments.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Avoid double-booking by locking time slots during the booking process.  
- Send email confirmation with appointment details.

---

### User Story 11

**Title:**  
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**  
1. Upcoming appointments are listed on the patient dashboard after login.  
2. Each appointment includes date, time, doctor name, and location/medium.  
3. Appointments are sorted by date and only future bookings are shown.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Optionally allow export to calendar or add reminder notifications.

## Doctor User Stories

### User Story 12

**Title:**  
_As a doctor, I want to log into the portal, so that I can manage my appointments._

**Acceptance Criteria:**  
1. The login form accepts a registered email and password.  
2. On successful login, the doctor is redirected to their dashboard.  
3. Invalid login attempts are handled with clear error messages.

**Priority:** High  
**Story Points:** 1  
**Notes:**  
- Ensure secure authentication and session handling.  
- Consider multi-factor authentication in the future.

---

### User Story 13

**Title:**  
_As a doctor, I want to log out of the portal, so that I can protect my data._

**Acceptance Criteria:**  
1. A logout button is clearly visible on all dashboard pages.  
2. Logging out ends the session and redirects to the login or landing page.  
3. Accessing protected routes after logout redirects to login.

**Priority:** High  
**Story Points:** 1  
**Notes:**  
- Include automatic logout after a period of inactivity.

---

### User Story 14

**Title:**  
_As a doctor, I want to view my appointment calendar, so that I can stay organized._

**Acceptance Criteria:**  
1. The calendar view shows daily/weekly appointments in a clear format.  
2. Each appointment includes time, patient name, and type.  
3. The calendar is dynamically updated based on new bookings or cancellations.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Use a color-coded system for appointment status (e.g., confirmed, canceled).  
- Consider integration with external calendar tools.

---

### User Story 15

**Title:**  
_As a doctor, I want to mark my unavailability, so that patients can only book during available slots._

**Acceptance Criteria:**  
1. Doctors can block out specific dates or time ranges in their schedule.  
2. Blocked slots are excluded from patient booking options.  
3. Confirmation is shown after unavailability is saved.

**Priority:** High  
**Story Points:** 3  
**Notes:**  
- Prevent existing appointments from being overwritten by unavailability.  
- Add recurrence options (e.g., weekly unavailability).

---

### User Story 16

**Title:**  
_As a doctor, I want to update my profile with specialization and contact information, so that patients have up-to-date information._

**Acceptance Criteria:**  
1. Doctors can access and edit profile fields like specialization, bio, and contact.  
2. Updated information is reflected in the patient-visible doctor list.  
3. Success and error messages are displayed appropriately on form submission.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Validate fields (e.g., phone/email format).  
- Include profile picture upload option if not already present.

---

### User Story 17

**Title:**  
_As a doctor, I want to view the patient details for upcoming appointments, so that I can be prepared._

**Acceptance Criteria:**  
1. Each appointment on the calendar links to the patient’s basic details.  
2. Details include name, age, contact info, and optionally medical history or notes.  
3. Information is viewable only by the assigned doctor.

**Priority:** Medium  
**Story Points:** 2  
**Notes:**  
- Ensure patient data privacy and access control.  
- Consider a printable view for offline preparation.
