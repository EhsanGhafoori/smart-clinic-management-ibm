# Database Schema Design - Smart Clinic Management System

## MySQL Database Schema

### Table: doctors

| Field | Type | Constraints | Description |
|-------|------|-------------|------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique doctor identifier |
| name | VARCHAR(100) | NOT NULL | Doctor's full name |
| email | VARCHAR(100) | NOT NULL, UNIQUE | Doctor's email address |
| password | VARCHAR(255) | NOT NULL | Encrypted password |
| specialty | VARCHAR(100) | NOT NULL | Medical specialty |
| phone | VARCHAR(20) | | Contact phone number |
| available_times | JSON | | Available time slots |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Last update time |

### Table: patients

| Field | Type | Constraints | Description |
|-------|------|-------------|------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique patient identifier |
| name | VARCHAR(100) | NOT NULL | Patient's full name |
| email | VARCHAR(100) | NOT NULL, UNIQUE | Patient's email address |
| password | VARCHAR(255) | NOT NULL | Encrypted password |
| phone | VARCHAR(20) | | Contact phone number |
| date_of_birth | DATE | | Patient's date of birth |
| address | VARCHAR(255) | | Patient's address |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Last update time |

### Table: appointments

| Field | Type | Constraints | Description |
|-------|------|-------------|------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique appointment identifier |
| doctor_id | BIGINT | FOREIGN KEY REFERENCES doctors(id) | Reference to doctor |
| patient_id | BIGINT | FOREIGN KEY REFERENCES patients(id) | Reference to patient |
| appointment_time | DATETIME | NOT NULL | Scheduled appointment date and time |
| status | VARCHAR(20) | DEFAULT 'scheduled' | Appointment status (scheduled, completed, cancelled) |
| notes | TEXT | | Additional appointment notes |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Last update time |

### Table: prescriptions

| Field | Type | Constraints | Description |
|-------|------|-------------|------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique prescription identifier |
| doctor_id | BIGINT | FOREIGN KEY REFERENCES doctors(id) | Reference to prescribing doctor |
| patient_id | BIGINT | FOREIGN KEY REFERENCES patients(id) | Reference to patient |
| appointment_id | BIGINT | FOREIGN KEY REFERENCES appointments(id) | Reference to related appointment |
| medication | VARCHAR(255) | NOT NULL | Medication name |
| dosage | VARCHAR(100) | | Medication dosage |
| instructions | TEXT | | Prescription instructions |
| prescribed_date | DATE | NOT NULL | Date prescription was issued |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Last update time |

## Relationships

- **doctors** (1) : (N) **appointments** - One doctor can have many appointments
- **patients** (1) : (N) **appointments** - One patient can have many appointments
- **doctors** (1) : (N) **prescriptions** - One doctor can write many prescriptions
- **patients** (1) : (N) **prescriptions** - One patient can have many prescriptions
- **appointments** (1) : (N) **prescriptions** - One appointment can have multiple prescriptions
