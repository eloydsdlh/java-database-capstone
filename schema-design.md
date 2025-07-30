# Schema Design

## MySQL Database Design

### Table: patients

- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), NOT NULL
- last_name: VARCHAR(50), NOT NULL
- date_of_birth: DATE, NOT NULL
- gender: ENUM('Male', 'Female', 'Other'), NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- phone: VARCHAR(15), UNIQUE
- address: VARCHAR(100)
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

---

### Table: doctors

- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(50), NOT NULL
- last_name: VARCHAR(50), NOT NULL
- specialty: VARCHAR(100), NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- phone: VARCHAR(15), UNIQUE
- location_id: INT, Foreign Key → clinic_locations(id)
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

---

### Table: appointments

- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id), NOT NULL
- patient_id: INT, Foreign Key → patients(id), NOT NULL
- appointment_time: DATETIME, NOT NULL
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

---

### Table: admin

- id: INT, Primary Key, Auto Increment
- username: VARCHAR(50), UNIQUE, NOT NULL
- password_hash: VARCHAR(255), NOT NULL
- email: VARCHAR(100), UNIQUE, NOT NULL
- created_at: TIMESTAMP, DEFAULT CURRENT_TIMESTAMP

---

### Table: clinic_locations

- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), NOT NULL
- address: VARCHAR(100), NOT NULL
- phone: VARCHAR(15)

## MongoDB Collection Design

### Collection: messages

```json
{
  "_id": "ObjectId('64f1bc12a9e34a9b77e8cd10')",
  "conversationId": "conv_20250725_001",
  "senderId": 17,
  "receiverId": 6,
  "appointmentId": 123,
  "timestamp": "2025-07-25T15:45:30Z",
  "message": {
    "text": "Hi Dr. Lee, I'm still experiencing a bit of pain. Should I increase my dosage?",
    "attachments": [
      {
        "fileName": "pain_chart.png",
        "fileType": "image/png",
        "fileUrl": "https://clinicstorage.example.com/uploads/pain_chart.png"
      }
    ]
  },
  "metadata": {
    "isRead": true,
    "readAt": "2025-07-25T16:10:00Z",
    "tags": ["follow-up", "pain-report"]
  }
}
```

### Collection: prescriptions

```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  }
}
```
