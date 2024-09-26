-- TODO: Remove this script before deploying to production.
-- This script consists of dummy data to populate the tables in the database for testing the application.


-- INSERT department
INSERT INTO department(name)
VALUES ('Medical'),
       ('Security'),
       ('Electrical'),
       ('Sanitation'),
       ('Operations'),
       ('Maintenance'),
       ('Catering'),
       ('Human Resources'),
       ('General');


-- INSERT category
INSERT INTO category (name, department_id)
VALUES ('Medical Assistance', (SELECT id FROM department WHERE name = 'Medical')),
       ('Security', (SELECT id FROM department WHERE name = 'Security')),
       ('Divyangjan Facilities', (SELECT id FROM department WHERE name = 'Security')),
       ('Facilities for Women with Special Needs', (SELECT id FROM department WHERE name = 'Security')),
       ('Electrical Equipment', (SELECT id FROM department WHERE name = 'Electrical')),
       ('Coach - Cleanliness', (SELECT id FROM department WHERE name = 'Sanitation')),
       ('Water Availability', (SELECT id FROM department WHERE name = 'Sanitation')),
       ('Punctuality', (SELECT id FROM department WHERE name = 'Operations')),
       ('Coach - Maintenance', (SELECT id FROM department WHERE name = 'Maintenance')),
       ('Catering & Vending Services', (SELECT id FROM department WHERE name = 'Catering')),
       ('Staff Behaviour', (SELECT id FROM department WHERE name = 'Human Resources')),
       ('Corruption / Bribery', (SELECT id FROM department WHERE name = 'Human Resources')),
       ('Bed Roll', (SELECT id FROM department WHERE name = 'Human Resources')),
       ('Miscellaneous', (SELECT id FROM department WHERE name = 'General'));


-- INSERT officer
INSERT INTO officer (date_registered, department_id, first_name, last_name, password_hash, last_login)
VALUES ('2023-01-10 09:30:00+05:30', (SELECT id FROM department WHERE name = 'Medical'), 'Amit', 'Sharma',
        'passwordhash1', '2023-09-20 10:15:00+05:30'),
       ('2023-02-15 11:00:00+05:30', (SELECT id FROM department WHERE name = 'Security'), 'Rajesh', 'Singh',
        'passwordhash2', '2023-09-21 12:45:00+05:30'),
       ('2023-03-20 14:25:00+05:30', (SELECT id FROM department WHERE name = 'Security'), 'Suman', 'Verma',
        'passwordhash3', '2023-09-19 08:30:00+05:30'),
       ('2023-04-10 17:40:00+05:30', (SELECT id FROM department WHERE name = 'Electrical'), 'Kavita', 'Patel',
        'passwordhash4', '2023-09-22 09:20:00+05:30'),
       ('2023-05-05 08:15:00+05:30', (SELECT id FROM department WHERE name = 'Sanitation'), 'Pooja', 'Naik',
        'passwordhash5', '2023-09-18 11:35:00+05:30'),
       ('2023-06-12 10:00:00+05:30', (SELECT id FROM department WHERE name = 'Sanitation'), 'Vijay', 'Bansal',
        'passwordhash6', '2023-09-17 16:45:00+05:30'),
       ('2023-07-18 13:35:00+05:30', (SELECT id FROM department WHERE name = 'Operations'), 'Rakesh', 'Gupta',
        'passwordhash7', '2023-09-16 14:50:00+05:30'),
       ('2023-08-25 07:50:00+05:30', (SELECT id FROM department WHERE name = 'Maintenance'), 'Sunita', 'Mehta',
        'passwordhash8', '2023-09-15 08:10:00+05:30'),
       ('2023-09-01 16:45:00+05:30', (SELECT id FROM department WHERE name = 'Catering'), 'Manoj', 'Mishra',
        'passwordhash9', '2023-09-14 17:25:00+05:30'),
       ('2023-09-10 12:25:00+05:30', (SELECT id FROM department WHERE name = 'Human Resources'), 'Neha', 'Kapoor',
        'passwordhash10', '2023-09-13 11:00:00+05:30'),
       ('2023-09-15 15:10:00+05:30', (SELECT id FROM department WHERE name = 'Human Resources'), 'Arjun', 'Iyer',
        'passwordhash11', '2023-09-12 12:30:00+05:30'),
       ('2023-09-20 11:20:00+05:30', (SELECT id FROM department WHERE name = 'Human Resources'), 'Priya', 'Jain',
        'passwordhash12', '2023-09-11 16:45:00+05:30'),
       ('2023-09-25 14:05:00+05:30', (SELECT id FROM department WHERE name = 'General'), 'Rahul', 'Saxena',
        'passwordhash13', '2023-09-10 14:35:00+05:30'),
       ('2023-09-27 18:30:00+05:30', (SELECT id FROM department WHERE name = 'General'), 'Anita', 'Rao',
        'passwordhash14', '2023-09-09 09:45:00+05:30'),
       ('2023-09-28 10:50:00+05:30', (SELECT id FROM department WHERE name = 'General'), 'Ashok', 'Menon',
        'passwordhash15', '2023-09-08 10:20:00+05:30');


-- INSERT customer
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO customer (date_registered, last_login, phone_number, first_name, last_name, password_hash, id)
VALUES ('2023-01-15 09:10:00+05:30', '2023-09-20 12:30:00+05:30', 9876543210, 'Rohit', 'Kumar', 'passwordhash1',
        gen_random_uuid()),
       ('2023-02-20 14:25:00+05:30', '2023-09-21 10:15:00+05:30', 9876543211, 'Sneha', 'Reddy', 'passwordhash2',
        gen_random_uuid()),
       ('2023-03-10 17:35:00+05:30', '2023-09-19 08:50:00+05:30', 9876543212, 'Vikram', 'Patil', 'passwordhash3',
        gen_random_uuid()),
       ('2023-04-18 08:45:00+05:30', '2023-09-22 09:00:00+05:30', 9876543213, 'Anjali', 'Desai', 'passwordhash4',
        gen_random_uuid()),
       ('2023-05-25 11:00:00+05:30', '2023-09-18 16:35:00+05:30', 9876543214, 'Pankaj', 'Singh', 'passwordhash5',
        gen_random_uuid()),
       ('2023-06-05 10:05:00+05:30', '2023-09-17 18:20:00+05:30', 9876543215, 'Meera', 'Chauhan', 'passwordhash6',
        gen_random_uuid()),
       ('2023-07-15 12:10:00+05:30', '2023-09-16 14:40:00+05:30', 9876543216, 'Rahul', 'Joshi', 'passwordhash7',
        gen_random_uuid()),
       ('2023-08-10 09:20:00+05:30', '2023-09-15 11:10:00+05:30', 9876543217, 'Sonal', 'Shah', 'passwordhash8',
        gen_random_uuid()),
       ('2023-09-01 15:40:00+05:30', '2023-09-14 17:50:00+05:30', 9876543218, 'Arvind', 'Nair', 'passwordhash9',
        gen_random_uuid()),
       ('2023-09-05 07:50:00+05:30', '2023-09-13 09:30:00+05:30', 9876543219, 'Ishita', 'Roy', 'passwordhash10',
        gen_random_uuid());


-- INSERT complaint
INSERT INTO complaint (category_id, date_filed, last_updated, officer_id, customer_id, status, summary, title)
VALUES
-- PENDING complaints (no category_id, officer_id, last_updated)
(null, '2024-09-01 10:30:00+05:30', null, null, (SELECT id FROM customer WHERE phone_number = 9876543210), 'PENDING',
 'The coach cleanliness was not satisfactory.', 'Unclean coach.'),
(null, '2024-09-02 12:45:00+05:30', null, null, (SELECT id FROM customer WHERE phone_number = 9876543211), 'PENDING',
 'Water supply was interrupted during the journey.', 'No water in the washroom.'),
(null, '2024-09-03 15:50:00+05:30', null, null, (SELECT id FROM customer WHERE phone_number = 9876543212), 'PENDING',
 'The electrical sockets were non-functional.', 'Faulty electrical sockets.'),
(null, '2024-09-04 17:20:00+05:30', null, null, (SELECT id FROM customer WHERE phone_number = 9876543213), 'PENDING',
 'Train was delayed by 3 hours without any updates.', 'Train delay notification missing.'),
(null, '2024-09-05 18:10:00+05:30', null, null, (SELECT id FROM customer WHERE phone_number = 9876543214), 'PENDING',
 'Food quality was below standard.', 'Poor quality of catering service.'),

-- IN_PROGRESS complaints (with category_id, officer_id)
((SELECT id FROM category WHERE name = 'Coach - Cleanliness'), '2024-08-20 09:15:00+05:30', '2024-09-10 12:45:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Sanitation') LIMIT 1),
 (SELECT id FROM customer WHERE phone_number = 9876543215), 'IN_PROGRESS', 'The coach was found dirty after boarding.',
 'Coach cleanliness issue.'),
((SELECT id FROM category WHERE name = 'Catering & Vending Services'), '2024-08-15 13:30:00+05:30',
 '2024-09-12 11:10:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Catering') LIMIT 1),
 (SELECT id FROM customer WHERE phone_number = 9876543216), 'IN_PROGRESS', 'Meals were cold and lacked quality.',
 'Poor quality catering.'),
((SELECT id FROM category WHERE name = 'Punctuality'), '2024-08-10 07:50:00+05:30', '2024-09-11 09:00:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Operations') LIMIT 1),
 (SELECT id FROM customer WHERE phone_number = 9876543217), 'IN_PROGRESS',
 'The train arrived 2 hours late at the destination.', 'Train delay issue.'),
((SELECT id FROM category WHERE name = 'Coach - Maintenance'), '2024-08-01 11:20:00+05:30', '2024-09-09 14:25:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Maintenance') LIMIT 1),
 (SELECT id FROM customer WHERE phone_number = 9876543218), 'IN_PROGRESS', 'The coach AC system was not functional.',
 'Faulty AC in coach.'),
((SELECT id FROM category WHERE name = 'Electrical Equipment'), '2024-08-25 16:40:00+05:30',
 '2024-09-08 15:00:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Electrical') LIMIT 1),
 (SELECT id FROM customer WHERE phone_number = 9876543219), 'IN_PROGRESS', 'None of the charging points worked.',
 'Non-functional charging points.'),

-- CLOSED complaints (with category_id, officer_id)
((SELECT id FROM category WHERE name = 'Security'), '2024-07-15 19:00:00+05:30', '2024-08-10 16:10:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Security') LIMIT 1),
 (SELECT id FROM customer WHERE phone_number = 9876543210), 'CLOSED',
 'A co-passenger was harassing me, and security acted swiftly.', 'Harassment case resolved.'),
((SELECT id FROM category WHERE name = 'Medical Assistance'), '2024-07-10 15:35:00+05:30', '2024-08-09 14:50:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Medical') LIMIT 1),
 (SELECT id FROM customer WHERE phone_number = 9876543211), 'CLOSED', 'Medical help was provided after I felt unwell.',
 'Medical assistance successful.'),
((SELECT id FROM category WHERE name = 'Water Availability'), '2024-07-05 08:10:00+05:30', '2024-08-08 12:15:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Sanitation') LIMIT 1),
 (SELECT id FROM customer WHERE phone_number = 9876543212), 'CLOSED', 'Water was restored after reporting the issue.',
 'Water supply issue resolved.'),
((SELECT id FROM category WHERE name = 'Staff Behaviour'), '2024-06-30 10:25:00+05:30', '2024-08-07 17:35:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Human Resources') LIMIT 1),
 (SELECT id FROM customer WHERE phone_number = 9876543213), 'CLOSED', 'A staff member was rude and was reported.',
 'Rude staff behavior issue addressed.'),
((SELECT id FROM category WHERE name = 'Bed Roll'), '2024-06-20 11:45:00+05:30', '2024-08-06 09:10:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Human Resources') LIMIT 1),
 (SELECT id FROM customer WHERE phone_number = 9876543214), 'CLOSED', 'The bedroll provided was unclean.',
 'Unclean bedroll issue resolved.');


-- INSERT media
INSERT INTO media (complaint_id, media_type, data)
VALUES
    -- Media for complaint_id 1 (PENDING)
    (1, 'IMAGE', gen_random_bytes(100)),
    (1, 'TEXT', gen_random_bytes(100)),
    -- Media for complaint_id 2 (PENDING)
    (2, 'VIDEO', gen_random_bytes(100)),
    -- Media for complaint_id 3 (PENDING)
    (3, 'AUDIO', gen_random_bytes(100)),
    -- Media for complaint_id 4 (PENDING)
    (4, 'TEXT', gen_random_bytes(100)),
    -- Media for complaint_id 5 (PENDING)
    (5, 'IMAGE', gen_random_bytes(100)),
    -- Media for complaint_id 6 (IN_PROGRESS)
    (6, 'VIDEO', gen_random_bytes(100)),
    -- Media for complaint_id 7 (IN_PROGRESS)
    (7, 'AUDIO', gen_random_bytes(100)),
    (7, 'IMAGE', gen_random_bytes(100)),
    -- Media for complaint_id 8 (IN_PROGRESS)
    (8, 'TEXT', gen_random_bytes(100)),
    -- Media for complaint_id 9 (IN_PROGRESS)
    (9, 'IMAGE', gen_random_bytes(100)),
    -- Media for complaint_id 10 (IN_PROGRESS)
    (10, 'VIDEO', gen_random_bytes(100)),
    -- Media for complaint_id 11 (CLOSED)
    (11, 'TEXT', gen_random_bytes(100)),
    (11, 'IMAGE', gen_random_bytes(100)),
    -- Media for complaint_id 12 (CLOSED)
    (12, 'VIDEO', gen_random_bytes(100)),
    -- Media for complaint_id 13 (CLOSED)
    (13, 'AUDIO', gen_random_bytes(100)),
    -- Media for complaint_id 14 (CLOSED)
    (14, 'IMAGE', gen_random_bytes(100)),
    -- Media for complaint_id 15 (CLOSED)
    (15, 'TEXT', gen_random_bytes(100));


-- INSERT message
INSERT INTO message (complaint_id, date_commented, officer_id, body)
VALUES
-- Messages for CLOSED complaints
(10, '2024-09-05 10:00:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Sanitation') LIMIT 1),
 'The cleanliness issue has been addressed and resolved.'),
(11, '2024-09-06 11:30:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Medical') LIMIT 1),
 'Medical assistance was provided promptly.'),
(12, '2024-09-07 12:45:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Sanitation') LIMIT 1),
 'Water supply issue has been resolved successfully.'),
(13, '2024-09-08 14:20:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Human Resources') LIMIT 1),
 'Staff behavior issue has been addressed and appropriate actions taken.'),
(14, '2024-09-09 15:50:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Human Resources') LIMIT 1),
 'The bed roll issue has been resolved and cleaned.'),

-- Messages for IN_PROGRESS complaints
(6, '2024-09-01 09:00:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Catering') LIMIT 1),
 'We are looking into the catering complaints and will resolve it shortly.'),
(7, '2024-09-02 09:30:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Operations') LIMIT 1),
 'Investigating the punctuality issues reported.'),
(8, '2024-09-03 10:15:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Maintenance') LIMIT 1),
 'Maintenance is currently assessing the coach issues.'),
(9, '2024-09-04 11:00:00+05:30',
 (SELECT id FROM officer WHERE department_id = (SELECT id FROM department WHERE name = 'Electrical') LIMIT 1),
 'We are working on fixing the electrical equipment complaints.');
